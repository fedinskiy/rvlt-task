package ru.fedinskiy.transfer;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.fedinskiy.validation.AccountNotFoundException;
import ru.fedinskiy.validation.InputValidation;
import ru.fedinskiy.validation.InvalidAccountIdException;
import ru.fedinskiy.validation.InvalidAmountException;

import javax.inject.Inject;
import java.text.MessageFormat;

@Controller("/transfer")
public class TransferController implements InputValidation {
	private static final Logger LOGGER = LoggerFactory.getLogger(TransferController.class);
	private final TransactionProcessor processor;

	@Inject
	public TransferController(TransactionProcessor processor) {
		this.processor = processor;
	}

	@Post("/{fromId}/{toId}")
	@Consumes(MediaType.TEXT_PLAIN)
	public HttpResponse transfer(@PathVariable(name = "fromId") String rawFromId,
	                             @PathVariable(name = "toId") String rawToId,
	                             String amount) {
		LOGGER.warn("Transferring {} from {} to {}", amount, rawFromId, rawToId);

		try {
			final boolean success = processor.transferMoney(parseId(rawFromId),
			                                                parseId(rawToId),
			                                                parseAmount(amount));
			if (success) {
				return HttpResponse
						.status(HttpStatus.OK)
						.body(MessageFormat.format("Transferred {0} from {1} to {2}", amount, rawFromId, rawToId));
			} else {
				return HttpResponse.status(HttpStatus.ACCEPTED).body("Money was not transferred");
			}
		} catch (AccountNotFoundException e) {
			return HttpResponse.notFound("There is no account with id " + e.getId());
		} catch (InvalidAccountIdException | InvalidAmountException e) {
			return HttpResponse.status(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
}

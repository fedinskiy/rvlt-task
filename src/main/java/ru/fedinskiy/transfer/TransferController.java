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

import javax.inject.Inject;
import java.text.MessageFormat;

@Controller("/transfer")
public class TransferController {
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
		LOGGER.warn("transfering {} from {} to {}", amount, rawFromId, rawToId);

		processor.transferMoney(Integer.parseInt(rawFromId), Integer.parseInt(rawToId), Integer.parseInt(amount));
		return HttpResponse
				.status(HttpStatus.ACCEPTED)
				.body(MessageFormat.format("transfered {0} from {1} to {2}", amount, rawFromId, rawToId));
	}
}

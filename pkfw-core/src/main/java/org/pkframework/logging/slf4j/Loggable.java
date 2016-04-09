package org.pkframework.logging.slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Loggable {

	protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

}

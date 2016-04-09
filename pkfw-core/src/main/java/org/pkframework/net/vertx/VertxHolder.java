package org.pkframework.net.vertx;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

public class VertxHolder {

	private static Vertx vertx;

	static {
		VertxOptions vertxOptions = new VertxOptions();

		vertx = Vertx.vertx(vertxOptions);
	}

	public static Vertx getVertx() {
		return vertx;
	}

}

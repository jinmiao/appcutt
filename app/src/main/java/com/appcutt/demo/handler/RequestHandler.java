package com.appcutt.demo.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public abstract class RequestHandler extends Handler {
	public RequestHandler() {
		super();
	}

	public RequestHandler(Looper looper) {
		super(looper);
	}

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);

		if (msg.obj == null) {
			return;
		}

	}

}

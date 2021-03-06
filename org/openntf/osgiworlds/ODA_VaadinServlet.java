package org.openntf.osgiworlds;

/*

<!--
Copyright 2015 Paul Withers
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and limitations under the License
-->

*/

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openntf.domino.utils.Factory;
import org.openntf.domino.xsp.ODAPlatform;

import com.vaadin.server.VaadinServlet;

/**
 * @author Paul Withers<br/>
 *         <br/>
 *         OsgiWorlds extension of VaadinServlet class, allowing us to start and
 *         terminate Domino threads and application configuration automatically.
 *
 */
public class ODA_VaadinServlet extends VaadinServlet {
	private static final long serialVersionUID = 1L;
	private boolean stopODAPlatform = false;

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.GenericServlet#init()
	 */
	@Override
	public void init() throws ServletException {
		if (!ODAPlatform.isStarted()) {
			ODAPlatform.start();
			stopODAPlatform = true;
		}
		super.init();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.vaadin.server.VaadinServlet#destroy()
	 */
	@Override
	public void destroy() {
		super.destroy();
		if (stopODAPlatform) {
			ODAPlatform.stop();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.vaadin.server.VaadinServlet#service(javax.servlet.http.
	 * HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		startDominoThread(request);
		super.service(request, response);
		stopDominoThread();
	}

	/**
	 * Initialises the Domino Thread based on application config
	 *
	 * @param request
	 *            HttpServletRequest current request
	 */
	public void startDominoThread(HttpServletRequest request) {
		try {
			Factory.initThread(Factory.STRICT_THREAD_CONFIG);
			final DefaultDominoApplicationConfig config = new DefaultDominoApplicationConfig();
			config.configure(getServletContext(), request);
		} catch (final Exception e) {
			stopDominoThread();
		}
	}

	/**
	 * Terminates the Domino Thread
	 */
	public void stopDominoThread() {
		try {
			Factory.termThread();
		} catch (final Exception e) {
			// TODO: handle exception
		}
	}
}

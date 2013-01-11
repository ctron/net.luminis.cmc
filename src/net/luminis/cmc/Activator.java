/*
 * Copyright (c) 2008 luminis
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * - Neither the name of the Luminis nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package net.luminis.cmc;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		
		boolean registered = false;
        boolean classNotFoundError = false;

		try {
			Class cmdClass = Class.forName("org.apache.felix.shell.Command");
			context.registerService(cmdClass.getName(), new FelixCmCommand(context), null);
			registered = true;
		}
		catch (ClassNotFoundException felixError) {
			// No Felix command class, can happen.
            classNotFoundError = true;
		}

		try {
			Class cmdClass = Class.forName("org.eclipse.osgi.framework.console.CommandProvider");
			context.registerService(cmdClass.getName(), new EclipseCmCommand(context), null);
			registered = true;
		}
		catch (ClassNotFoundException eclipseError) {
			// No Equinox command class, can happen
            classNotFoundError = true;
		}

        GogoCmCommand gogoCmCommand = new GogoCmCommand(context);
        context.registerService(GogoCmCommand.class.getName(), gogoCmCommand, gogoCmCommand.getServiceProperties());

        if (context.getServiceReference("org.apache.felix.service.command.CommandProcessor") != null) {
            // Assume Gogo will be functional
            registered = true;
        }

		if (! registered) {
            if (classNotFoundError) {
                System.err.println("Could not register command (command class not found). ");
            }
			System.err.println("Note that this bundle only works with the Felix or Equinox command shell, or with an RFC 147 implementation.");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
	}

}

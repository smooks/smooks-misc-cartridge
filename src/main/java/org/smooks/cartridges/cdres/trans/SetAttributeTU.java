/*-
 * ========================LICENSE_START=================================
 * smooks-misc-cartridge
 * %%
 * Copyright (C) 2020 Smooks
 * %%
 * Licensed under the terms of the Apache License Version 2.0, or
 * the GNU Lesser General Public License version 3.0 or later.
 * 
 * SPDX-License-Identifier: Apache-2.0 OR LGPL-3.0-or-later
 * 
 * ======================================================================
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * ======================================================================
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * =========================LICENSE_END==================================
 */
/*
	Milyn - Copyright (C) 2006 - 2010

	This library is free software; you can redistribute it and/or
	modify it under the terms of the GNU Lesser General Public
	License (version 2.1) as published by the Free Software 
	Foundation.

	This library is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
    
	See the GNU Lesser General Public License for more details:    
	http://www.gnu.org/licenses/lgpl.txt
*/

package org.smooks.cartridges.cdres.trans;

import org.smooks.cdr.annotation.ConfigParam;
import org.smooks.container.ExecutionContext;
import org.smooks.delivery.dom.DOMElementVisitor;
import org.w3c.dom.Element;

/**
 * Set a DOM element attribute <u>during the processing phase</u>.
 * <h3>.cdrl Configuration</h3>
 * <pre>
 * &lt;smooks-resource	useragent="<i>device/profile</i>" selector="<i>target-element-name</i>" 
 * 	path="org.smooks.cdres.trans.SetAttributeTU"&gt;
 * 
 * 	&lt;!-- The name of the element attribute to be set. --&gt;
 * 	&lt;param name="<b>attributeName</b>"&gt;<i>attribute-name</i>&lt;/param&gt;
 * 
 * 	&lt;!-- The value of the element attribute to be set. --&gt;
 * 	&lt;param name="<b>attributeValue</b>"&gt;<i>attribute-value</i>&lt;/param&gt;
 * 
 * 	&lt;!-- (Optional) Overwrite existing attributes of the same name. Default false. --&gt;
 * 	&lt;param name="<b>overwrite</b>"&gt;<i>true/false</i>&lt;/param&gt;
 * 
 * 	&lt;!-- (Optional) Visit the target element before iterating over the elements
 * 		child content. Default false. --&gt;
 * 	&lt;param name="<b>visitBefore</b>"&gt;<i>true/false</i>&lt;/param&gt;
 * &lt;/smooks-resource&gt;</pre>
 * See {@link org.smooks.cdr.SmooksResourceConfiguration}.
 * @author tfennelly
 */
public class SetAttributeTU implements DOMElementVisitor {

    @ConfigParam
	private String attributeName;

    @ConfigParam
	private String attributeValue;

    @ConfigParam(use = ConfigParam.Use.OPTIONAL, defaultVal = "false")
	private boolean visitBefore;

    @ConfigParam(use = ConfigParam.Use.OPTIONAL, defaultVal = "false")
	private boolean overwrite;

    public void visitBefore(Element element, ExecutionContext executionContext) {
        if(visitBefore) {
            visit(element, executionContext);
        }
    }

    public void visitAfter(Element element, ExecutionContext executionContext) {
        if(!visitBefore) {
            visit(element, executionContext);
        }
    }

    private void visit(Element element, ExecutionContext executionContext) {
		// If the element already has the new attribute and we're
		// not overwriting, leave all as is and return.
		if(!overwrite && element.hasAttribute(attributeName)) {
			return;
		}
		element.setAttribute(attributeName, attributeValue);
	}
}

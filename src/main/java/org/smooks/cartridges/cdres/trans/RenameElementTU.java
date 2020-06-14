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
package org.smooks.cartridges.cdres.trans;

import org.smooks.cdr.annotation.ConfigParam;
import org.smooks.container.ExecutionContext;
import org.smooks.delivery.dom.DOMElementVisitor;
import org.smooks.xml.DomUtils;
import org.w3c.dom.Element;

/**
 * Renames/replaces an element in the document <u>during the processing phase</u>.
 * <p/>
 * The element is visited by this Processing Unit after it's child content
 * has been iterated over.
 * <p/>
 * See {@link DomUtils#renameElement(org.w3c.dom.Element, java.lang.String, boolean, boolean)}.
 * 
 * <h3>.cdrl Configuration</h3>
 * <pre>
 * &lt;smooks-resource	useragent="<i>device/profile</i>" selector="<i>target-element-name</i>" path="org.smooks.cdres.trans.RenameElementTU"&gt;
 * 
 * 	&lt;!-- The name of the replacement element. --&gt;
 * 	&lt;param name="<b>replacementElement</b>"&gt;<i>replacement-element-name</i>&lt;/param&gt;
 * 
 * 	&lt;!-- (Optional) Copy target elements child content to the replacement 
 * 		element. Default is true. --&gt;
 * 	&lt;param name="<b>keepChildContent</b>"&gt;<i>true/false</i>&lt;/param&gt;
 * 
 * 	&lt;!-- (Optional) Copy target elements attributes to the replacement 
 * 		element. Default is true. --&gt;
 * 	&lt;param name="<b>keepAttributes</b>"&gt;<i>true/false</i>&lt;/param&gt;
 * &lt;/smooks-resource&gt;</pre>
 * See {@link org.smooks.cdr.SmooksResourceConfiguration}.
 * 
 * @author tfennelly
 */
public class RenameElementTU implements DOMElementVisitor {

    @ConfigParam
    private String replacementElement;

    @ConfigParam(use = ConfigParam.Use.OPTIONAL, defaultVal = "true")
	private boolean keepChildContent;

    @ConfigParam(use = ConfigParam.Use.OPTIONAL, defaultVal = "true")
	private boolean keepAttributes;

    public void visitBefore(Element element, ExecutionContext executionContext) {
    }

	public void visitAfter(Element element, ExecutionContext request) {
		DomUtils.renameElement(element, replacementElement, keepChildContent, keepAttributes);
	}
}

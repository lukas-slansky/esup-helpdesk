<%@include file="_include.jsp"%>

<h:panelGroup >
	<h:outputText 
		styleClass="#{cpe.ticket.opened && !cpe.ticket.postponed ? priorityStyleClassProvider[cpe.ticket.effectivePriorityLevel] : ''}" 
		style="font-weight: #{cpe.viewed ? 'normal' : 'bold'}; font-style: #{(cpe.ticket.opened && !cpe.ticket.postponed) ? 'normal' : 'italic'};" 
		rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'ID'}"
		value="#{cpe.ticket.id} " />
	<h:outputText 
		styleClass="#{cpe.ticket.opened && !cpe.ticket.postponed ? priorityStyleClassProvider[cpe.ticket.effectivePriorityLevel] : ''}" 
		style="font-weight: #{cpe.viewed ? 'normal' : 'bold'}; font-style: #{(cpe.ticket.opened && !cpe.ticket.postponed) ? 'normal' : 'italic'};" 
		rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'DEPARTMENT'}" 
		value="#{cpe.ticket.department.label} " />
	<h:outputText 
		styleClass="#{cpe.ticket.opened && !cpe.ticket.postponed ? priorityStyleClassProvider[cpe.ticket.effectivePriorityLevel] : ''}" 
		style="font-weight: #{cpe.viewed ? 'normal' : 'bold'}; font-style: #{(cpe.ticket.opened && !cpe.ticket.postponed) ? 'normal' : 'italic'};" 
		rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'CREATION_DEPARTMENT'}"
		value="#{cpe.ticket.creationDepartment.label} " />
	<h:outputText 
		styleClass="#{cpe.ticket.opened && !cpe.ticket.postponed ? priorityStyleClassProvider[cpe.ticket.effectivePriorityLevel] : ''}" 
		style="font-weight: #{cpe.viewed ? 'normal' : 'bold'}; font-style: #{(cpe.ticket.opened && !cpe.ticket.postponed) ? 'normal' : 'italic'};" 
		rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'CATEGORY'}"
		value="#{cpe.ticket.category.label} " />
	<h:outputText 
		styleClass="#{cpe.ticket.opened && !cpe.ticket.postponed ? priorityStyleClassProvider[cpe.ticket.effectivePriorityLevel] : ''}" 
		style="font-weight: #{cpe.viewed ? 'normal' : 'bold'}; font-style: #{(cpe.ticket.opened && !cpe.ticket.postponed) ? 'normal' : 'italic'};" 
		rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'LABEL'}"
		value="#{controlPanelSubjectTruncator[cpe.ticket.label]} " />
	<h:outputText 
		styleClass="#{cpe.ticket.opened && !cpe.ticket.postponed ? priorityStyleClassProvider[cpe.ticket.effectivePriorityLevel] : ''}" 
		style="font-weight: #{cpe.viewed ? 'normal' : 'bold'}; font-style: #{(cpe.ticket.opened && !cpe.ticket.postponed) ? 'normal' : 'italic'};" 
		rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'STATUS'}"
		value="#{msgs[ticketStatusI18nKeyProvider[cpe.ticket.status]]} " />
	<h:outputText 
		styleClass="#{cpe.ticket.opened && !cpe.ticket.postponed ? priorityStyleClassProvider[cpe.ticket.effectivePriorityLevel] : ''}" 
		style="font-weight: #{cpe.viewed ? 'normal' : 'bold'}; font-style: #{(cpe.ticket.opened && !cpe.ticket.postponed) ? 'normal' : 'italic'};" 
		rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'PRIORITY'}"
		value="#{msgs[priorityI18nKeyProvider[cpe.ticket.effectivePriorityLevel]]} " />
	<h:outputText 
		styleClass="#{cpe.ticket.opened && !cpe.ticket.postponed ? priorityStyleClassProvider[cpe.ticket.effectivePriorityLevel] : ''}" 
		style="font-weight: #{cpe.viewed ? 'normal' : 'bold'}; font-style: #{(cpe.ticket.opened && !cpe.ticket.postponed) ? 'normal' : 'italic'};" 
		rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'CREATION_DATE_TIME'}"
		value="#{cpe.ticket.creationDate}" >
		<f:convertDateTime timeZone="#{sessionController.timezone}" type="both" dateStyle="short" locale="#{sessionController.locale}" />
	</h:outputText>
	<h:outputText 
		styleClass="#{cpe.ticket.opened && !cpe.ticket.postponed ? priorityStyleClassProvider[cpe.ticket.effectivePriorityLevel] : ''}" 
		style="font-weight: #{cpe.viewed ? 'normal' : 'bold'}; font-style: #{(cpe.ticket.opened && !cpe.ticket.postponed) ? 'normal' : 'italic'};" 
		rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'CREATION_DATE'}"
		value="#{cpe.ticket.creationDate}" >
		<f:convertDateTime timeZone="#{sessionController.timezone}" type="date" dateStyle="short" locale="#{sessionController.locale}"/>
	</h:outputText>
	<h:outputText 
		styleClass="#{cpe.ticket.opened && !cpe.ticket.postponed ? priorityStyleClassProvider[cpe.ticket.effectivePriorityLevel] : ''}" 
		style="font-weight: #{cpe.viewed ? 'normal' : 'bold'}; font-style: #{(cpe.ticket.opened && !cpe.ticket.postponed) ? 'normal' : 'italic'};" 
		rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'CHANGE_DATE_TIME'}"
		value="#{cpe.ticket.lastActionDate}" >
		<f:convertDateTime timeZone="#{sessionController.timezone}" type="both" dateStyle="short" locale="#{sessionController.locale}"/>
	</h:outputText>
	<h:outputText 
		styleClass="#{cpe.ticket.opened && !cpe.ticket.postponed ? priorityStyleClassProvider[cpe.ticket.effectivePriorityLevel] : ''}" 
		style="font-weight: #{cpe.viewed ? 'normal' : 'bold'}; font-style: #{(cpe.ticket.opened && !cpe.ticket.postponed) ? 'normal' : 'italic'};" 
		rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'CHANGE_DATE'}"
		value="#{cpe.ticket.lastActionDate}" >
		<f:convertDateTime timeZone="#{sessionController.timezone}" type="date" dateStyle="short" locale="#{sessionController.locale}"/>
	</h:outputText>
	<h:outputText
		styleClass="#{cpe.ticket.opened && !cpe.ticket.postponed ? priorityStyleClassProvider[cpe.ticket.effectivePriorityLevel] : ''}"
		style="font-weight: #{cpe.viewed ? 'normal' : 'bold'}; font-style: #{(cpe.ticket.opened && !cpe.ticket.postponed) ? 'normal' : 'italic'};"
		rendered="#{(controlPanelController.columnsOrderer[columnIndex] == 'OWNER') && (not controlPanelController.currentUserDepartmentManager || controlPanelController.currentUser.controlPanelUserInterface)}"
		value=" --- " />
	<h:outputText
		styleClass="#{cpe.ticket.opened && !cpe.ticket.postponed ? priorityStyleClassProvider[cpe.ticket.effectivePriorityLevel] : ''}"
		style="font-weight: #{cpe.viewed ? 'normal' : 'bold'}; font-style: #{(cpe.ticket.opened && !cpe.ticket.postponed) ? 'normal' : 'italic'};"
		rendered="#{(controlPanelController.columnsOrderer[columnIndex] == 'OWNER') && controlPanelController.currentUserDepartmentManager && not controlPanelController.currentUser.controlPanelUserInterface}"
		value="#{userFormatter[cpe.ticket.owner]} " />
	<h:outputText 
		styleClass="#{cpe.ticket.opened && !cpe.ticket.postponed ? priorityStyleClassProvider[cpe.ticket.effectivePriorityLevel] : ''}" 
		style="font-weight: #{cpe.viewed ? 'normal' : 'bold'}; font-style: #{(cpe.ticket.opened && !cpe.ticket.postponed) ? 'normal' : 'italic'};" 
		rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'MANAGER'}"
		value="#{userFormatter[cpe.ticket.manager]} " />
</h:panelGroup>

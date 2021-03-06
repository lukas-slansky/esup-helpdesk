/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain;

import java.util.ArrayList;
import java.util.List;

import org.esupportail.commons.aop.monitor.Monitor;
import org.esupportail.commons.dao.HqlUtils;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.helpdesk.domain.beans.Category;
import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.domain.beans.Invitation;
import org.esupportail.helpdesk.domain.beans.Ticket;
import org.esupportail.helpdesk.domain.beans.TicketMonitoring;
import org.esupportail.helpdesk.domain.beans.User;

/**
 * A bean to extract tickets for the control panel.
 */
@Monitor
public class TicketExtractorImpl extends AbstractTicketExtractor {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -5779505084297323900L;

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());

	/**
	 * Bean constructor.
	 */
	public TicketExtractorImpl() {
		super();
	}

	/**
	 * @return the condition for opened tickets.
	 */
	protected String getStatusOpenedCondition() {
		return HqlUtils.stringIn("ticket.status", new String[] {
				TicketStatus.FREE,
				TicketStatus.INCOMPLETE,
				TicketStatus.INPROGRESS,
				TicketStatus.POSTPONED,
		});
	}

	/**
	 * @param user
	 * @return the condition regarding the status.
	 */
	protected String getStatusCondition(final User user) {
		String statusFilter;
		if (user.getControlPanelUserInterface()) {
			statusFilter = user.getControlPanelUserStatusFilter();
		} else {
			statusFilter = user.getControlPanelManagerStatusFilter();
		}
		String condition;
		if (ControlPanel.STATUS_FILTER_OPENED.equals(statusFilter)) {
			condition = getStatusOpenedCondition();
		} else if (ControlPanel.STATUS_FILTER_CLOSED.equals(statusFilter)) {
			condition = HqlUtils.not(getStatusOpenedCondition());
		} else {
			condition = HqlUtils.alwaysTrue();
		}
		if (logger.isDebugEnabled()) {
			logger.debug("statusCondition = " + condition);
		}
		return condition;
	}

	/**
	 * @param departmentFilter
	 * @return the condition regarding the selected department.
	 */
	protected String getSelectedDepartmentCondition(
			final Department departmentFilter) {
		String condition;
		if (departmentFilter == null) {
			condition = HqlUtils.alwaysTrue();
		} else {
			condition = HqlUtils.equals("ticket.department.id", departmentFilter.getId());
		}
		if (logger.isDebugEnabled()) {
			logger.debug("selectedDepartmentCondition = " + condition);
		}
		return condition;
	}

	/**
	 * @param user
	 * @return the condition for the user to own the tickets.
	 */
	protected String getOwnerCondition(final User user) {
		String condition = HqlUtils.equals("ticket.owner.id", HqlUtils.quote(user.getId()));
		if (logger.isDebugEnabled()) {
			logger.debug("ownerCondition = " + condition);
		}
		return condition;
	}

	/**
	 * @param user
	 * @return the condition regarding monitoring.
	 */
	protected String getMonitoringCondition(
			final User user) {
		String condition = HqlUtils.exists(
				HqlUtils.selectFromWhere(
						"monitoring",
						TicketMonitoring.class.getSimpleName()
						+ HqlUtils.AS_KEYWORD
						+ "monitoring",
						HqlUtils.and(
								HqlUtils.equals(
										"monitoring.ticket.id",
								"ticket.id"),
								HqlUtils.equals(
										"monitoring.user.id",
										HqlUtils.quote(user.getId()))
						)
				)
		);
		if (logger.isDebugEnabled()) {
			logger.debug("monitoringCondition = " + condition);
		}
		return condition;
	}

	/**
	 * @param user
	 * @return the condition regarding invitations.
	 */
	protected String getInvitedCondition(
			final User user) {
		String condition = HqlUtils.exists(
				HqlUtils.selectFromWhere(
						"invitation",
						Invitation.class.getSimpleName()
						+ HqlUtils.AS_KEYWORD
						+ "invitation",
						HqlUtils.and(
								HqlUtils.equals(
										"invitation.ticket.id",
								"ticket.id"),
								HqlUtils.equals(
										"invitation.user.id",
										HqlUtils.quote(user.getId()))
						)
				)
		);
		if (logger.isDebugEnabled()) {
			logger.debug("invitedCondition = " + condition);
		}
		return condition;
	}

	/**
	 * @param user
	 * @return the condition regarding the managed departments.
	 */
	protected String getManagedDepartmentCondition(
			final User user) {
		List<Long> departmentsIds = new ArrayList<Long>();
		for (Department department : getDomainService().getManagedDepartments(user)) {
			departmentsIds.add(new Long(department.getId()));
		}
		String condition = HqlUtils.longIn("ticket.department.id", departmentsIds);
		if (logger.isDebugEnabled()) {
			logger.debug("managedDepartmentCondition = " + condition);
		}
		return condition;
	}

	/**
	 * @return true if the user can see the ticket.
	 */
	protected String getUserVisibleTicketCondition() {
		String condition = HqlUtils.not(
				HqlUtils.equals(
						"ticket.effectiveScope",
						HqlUtils.quote(TicketScope.PRIVATE)));
		if (logger.isDebugEnabled()) {
			logger.debug("userVisibleCondition = " + condition);
		}
		return condition;
	}

	/**
	 * @return true if the user can read the ticket.
	 */
	protected String getUserReadableTicketCondition() {
		String condition = HqlUtils.equals(
						"ticket.effectiveScope",
						HqlUtils.quote(TicketScope.PUBLIC));
		if (logger.isDebugEnabled()) {
			logger.debug("userReadableCondition = " + condition);
		}
		return condition;
	}

	/**
	 * @param user
	 * @return the condition for the user to manage the tickets.
	 */
	protected String getManagedCondition(final User user) {
		String condition = HqlUtils.equals("ticket.manager.id", HqlUtils.quote(user.getId()));
		if (logger.isDebugEnabled()) {
			logger.debug("managedCondition = " + condition);
		}
		return condition;
	}

	/**
	 * @return the condition for free tickets.
	 */
	protected String getFreeCondition() {
		String condition = HqlUtils.isNull("ticket.manager.id");
		if (logger.isDebugEnabled()) {
			logger.debug("freeCondition = " + condition);
		}
		return condition;
	}

	/**
	 * @param user
	 * @param selectedManager
	 * @return the condition regarding the involvement for managers.
	 */
	protected String getManagerInvolvementCondition(
			final User user,
			final User selectedManager) {
		String condition;
		String involvementFilter = user.getControlPanelManagerInvolvementFilter();
		if (ControlPanel.MANAGER_INVOLVEMENT_FILTER_FREE.equals(involvementFilter)) {
			if (selectedManager != null) {
				condition = HqlUtils.alwaysFalse();
			} else {
				condition = getFreeCondition();
			}
		} else if (ControlPanel.MANAGER_INVOLVEMENT_FILTER_MANAGED.equals(involvementFilter)) {
			if (selectedManager != null && !selectedManager.equals(user)) {
				condition = HqlUtils.alwaysFalse();
			} else {
				condition = getManagedCondition(user);
			}
		} else if (ControlPanel.MANAGER_INVOLVEMENT_FILTER_MANAGED_OR_FREE.equals(involvementFilter)) {
			if (selectedManager != null) {
				if (selectedManager.equals(user)) {
					condition = getManagedCondition(user);
				} else {
					condition = HqlUtils.alwaysFalse();
				}
			} else {
				condition = HqlUtils.or(
						getManagedCondition(user),
						getFreeCondition());
			}
		} else {
			if (selectedManager != null) {
				condition = getManagedCondition(selectedManager);
			} else {
				condition = HqlUtils.alwaysTrue();
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("managerInvolvementCondition = " + condition);
		}
		return condition;
	}

	/**
	 * @param user
	 * @param departmentFilter
	 * @return the condition regarding the category membership.
	 */
	protected String getCategoryMemberCondition(
			final User user,
			final Department departmentFilter) {
		List<Long> categoryIds = new ArrayList<Long>();
		for (Category category : getDomainService().getMemberCategories(user, departmentFilter)) {
			categoryIds.add(new Long(category.getId()));
		}
		String condition = HqlUtils.longIn("ticket.category.id", categoryIds);
		if (logger.isDebugEnabled()) {
			logger.debug("categoryMemberCondition = " + condition);
		}
		return condition;
	}

	/**
	 * @param category
	 * @param categoryIds
	 * @return the category tree of the given category, as a list
	 */
	protected List<Long> getCategoryTreeIds(final Category category, final List<Long> categoryIds) {
		List<Long> theCategoryIds = categoryIds;
		if (theCategoryIds == null) {
			theCategoryIds = new ArrayList<Long>();
		}
		theCategoryIds.add(category.getId());
		for (Category subCategory : getDomainService().getSubCategories(category)) {
			getCategoryTreeIds(subCategory, theCategoryIds);
		}
		return theCategoryIds;
	}

	/**
	 * @param user
	 * @return the condition regarding the visiblity for managers.
	 */
	protected String getManagerVisibleTicketCondition(
			final User user) {
		String condition;
		Department departmentFilter = user.getControlPanelManagerDepartmentFilter();
		List<Department> managedDepartments = getDomainService().getManagedDepartments(user);
		if (departmentFilter != null && !managedDepartments.contains(departmentFilter)) {
			departmentFilter = null;
		}
		if (departmentFilter == null) {
			condition = getManagedDepartmentCondition(user);
		} else {
			if (user.getControlPanelCategoryMemberFilter()) {
				condition = getCategoryMemberCondition(user, departmentFilter);
			} else {
				Category categoryFilter = user.getControlPanelCategoryFilter();
				if (categoryFilter != null
						&& !departmentFilter.equals(categoryFilter.getDepartment())) {
					categoryFilter = null;
				}
				if (categoryFilter == null) {
					condition = HqlUtils.equals("ticket.department.id", departmentFilter.getId());
				} else {
					condition = HqlUtils.longIn(
							"ticket.category.id", getCategoryTreeIds(categoryFilter, null));
				}
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("managerControlPanelDepartmentCondition = " + condition);
		}
		return condition;
	}

	/**
	 * @param user
	 * @return the order by string.
	 */
	protected String getControlPanelOrderByString(
			final User user) {
		String separator = "";
		String result = "";
		for (ControlPanelOrderPart orderPart : user.getControlPanelOrder().getOrderParts()) {
			String orderBy;
			if (ControlPanelOrder.ID.equals(orderPart.getName())) {
				orderBy = "ticket.id";
			} else if (ControlPanelOrder.DEPARTMENT.equals(orderPart.getName())) {
				orderBy = "ticket.department.label";
			} else if (ControlPanelOrder.CATEGORY.equals(orderPart.getName())) {
				orderBy = "ticket.category.label";
			} else if (ControlPanelOrder.CREATION_DEPARTMENT.equals(orderPart.getName())) {
				orderBy = "ticket.creationDepartment.label";
			} else if (ControlPanelOrder.LABEL.equals(orderPart.getName())) {
				orderBy = "ticket.label";
			} else if (ControlPanelOrder.LAST_ACTION_DATE.equals(orderPart.getName())) {
				orderBy = "ticket.lastActionDate";
			} else if (ControlPanelOrder.MANAGER.equals(orderPart.getName())) {
				orderBy = "ticket.managerDisplayName";
			} else if (ControlPanelOrder.OWNER.equals(orderPart.getName())) {
				orderBy = "ticket.owner.displayName";
			} else if (ControlPanelOrder.PRIORITY.equals(orderPart.getName())) {
				orderBy = "ticket.priorityLevel";
			} else if (ControlPanelOrder.STATUS.equals(orderPart.getName())) {
				orderBy = "ticket.status";
			} else {
				throw new IllegalArgumentException("bad order part [" + orderPart + "]");
			}
			result += separator + orderBy + " ";
			if (orderPart.isAsc()) {
				result += "ASC";
			} else {
				result += "DESC";
			}
			separator = ",";
		}
		return result;
	}

	/**
	 * @param user
	 * @param selectedManager
	 * @return the query string for managers.
	 */
	protected String getManagerControlPanelQueryString(
			final User user,
			final User selectedManager) {
		if (user == null) {
			return null;
		}
		String managerCondition = HqlUtils.and(
				getStatusCondition(user),
				getManagerInvolvementCondition(user, selectedManager),
				getManagerVisibleTicketCondition(user));
		if (logger.isDebugEnabled()) {
			logger.debug("managerCondition = " + managerCondition);
		}
		if (HqlUtils.isAlwaysFalse(managerCondition)) {
			return null;
		}
		return HqlUtils.fromWhereOrderBy(
				Ticket.class.getSimpleName() + HqlUtils.AS_KEYWORD + "ticket",
				managerCondition,
				getControlPanelOrderByString(user));
	}

	/**
	 * @param visibleDepartments
	 * @return the condition regarding the department for non managers.
	 */
	protected String getUserVisibleDepartmentCondition(
			final List<Department> visibleDepartments) {
		String condition;
		List<Long> departmentsIds = new ArrayList<Long>();
		for (Department department : visibleDepartments) {
			departmentsIds.add(new Long(department.getId()));
		}
		condition = HqlUtils.longIn("ticket.department.id", departmentsIds);
		if (logger.isDebugEnabled()) {
			logger.debug("userVisibleDepartmentCondition = " + condition);
		}
		return condition;
	}

	/**
	 * @param departmentFilter
	 * @param visibleDepartments
	 * @return the condition regarding the selected department for non managers.
	 */
	protected String getUserControlPanelVisibleDepartmentCondition(
			final Department departmentFilter,
			final List<Department> visibleDepartments) {
		String condition;
		if (departmentFilter == null) {
			condition = getUserVisibleDepartmentCondition(visibleDepartments);
		} else {
			condition = HqlUtils.alwaysTrue();
		}
		if (logger.isDebugEnabled()) {
			logger.debug("userControlPanelVisibleDepartmentCondition = " + condition);
		}
		return condition;
	}

	/**
	 * @param user
	 * @param departmentFilter
	 * @param visibleDepartments
	 * @return the condition regarding the involvement for non managers.
	 */
	protected String getUserControlPanelInvolvementCondition(
			final User user,
			final Department departmentFilter,
			final List<Department> visibleDepartments) {
		String condition;
		String involvementFilter = user.getControlPanelUserInvolvementFilter();
		if (ControlPanel.USER_INVOLVEMENT_FILTER_OWNER.equals(involvementFilter)) {
			condition = getOwnerCondition(user);
		} else if (ControlPanel.USER_INVOLVEMENT_FILTER_INVITED.equals(involvementFilter)) {
			condition = getInvitedCondition(user);
		} else if (ControlPanel.USER_INVOLVEMENT_FILTER_OWNER_OR_INVITED.equals(involvementFilter)) {
			condition = HqlUtils.or(
					getOwnerCondition(user),
					getInvitedCondition(user));
		} else if (ControlPanel.USER_INVOLVEMENT_FILTER_MONITORING.equals(involvementFilter)) {
			condition = HqlUtils.and(
					getMonitoringCondition(user),
					HqlUtils.or(
							getUserReadableTicketCondition(),
							getOwnerCondition(user),
							getInvitedCondition(user)
					)
			);
		} else {
			condition = HqlUtils.or(
					getOwnerCondition(user),
					getInvitedCondition(user),
					HqlUtils.and(
							getUserVisibleTicketCondition(),
							getUserControlPanelVisibleDepartmentCondition(
									departmentFilter, visibleDepartments)
							)
			);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("userInvolvementCondition = " + condition);
		}
		return condition;
	}

	/**
	 * @param user
	 * @param visibleDepartments
	 * @return the query string for non managers.
	 */
	protected String getUserControlPanelQueryString(
			final User user,
			final List<Department> visibleDepartments) {
		if (user == null) {
			return null;
		}
		Department departmentFilter = user.getControlPanelUserDepartmentFilter();
		if (departmentFilter != null && !visibleDepartments.contains(departmentFilter)) {
			departmentFilter = null;
		}
		String userCondition = HqlUtils.and(
				getStatusCondition(user),
				getSelectedDepartmentCondition(departmentFilter),
				getUserControlPanelInvolvementCondition(user, departmentFilter, visibleDepartments));
		if (logger.isDebugEnabled()) {
			logger.debug("userCondition = " + userCondition);
		}
		if (HqlUtils.isAlwaysFalse(userCondition)) {
			return null;
		}
		return HqlUtils.fromWhereOrderByDesc(
				Ticket.class.getSimpleName() + HqlUtils.AS_KEYWORD + "ticket",
				userCondition,
				getControlPanelOrderByString(user));
	}

	/**
	 * @see org.esupportail.helpdesk.domain.TicketExtractor#getControlPanelQueryString(
	 * org.esupportail.helpdesk.domain.beans.User, org.esupportail.helpdesk.domain.beans.User,
	 * java.util.List)
	 */
	@Override
	public String getControlPanelQueryString(
			final User user,
			final User selectedManager,
			final List<Department> visibleDepartments) {
		String queryString;
		if (user == null) {
			queryString = null;
		} else if (user.getControlPanelUserInterface() || !getDomainService().isDepartmentManager(user)) {
			queryString = getUserControlPanelQueryString(user, visibleDepartments);
		} else {
			User theSelectedManager = selectedManager;
			if (theSelectedManager != null) {
				if (user.getControlPanelManagerDepartmentFilter() == null) {
					theSelectedManager = null;
				} else if (!getDomainService().isDepartmentManager(
							user.getControlPanelManagerDepartmentFilter(),
							theSelectedManager)) {
					theSelectedManager = null;
				}
			}
			queryString = getManagerControlPanelQueryString(user, theSelectedManager);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("queryString = " + queryString);
		}
		return queryString;
	}

}


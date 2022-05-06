import pageUrls from "./pageUrls.js";
import {navigateTo} from "./common.js";

/**
 * 跳转到主页面
 * @returns {*}
 */
export function navigateToHome() {

    return navigateTo(pageUrls.home);
}

export function navigateToReport(reportId) {
    console.log(reportId)
    return navigateTo(pageUrls.reportDetail + "?reportId=" + reportId);
}

export function navigateToPublishReport() {

    return navigateTo(pageUrls.publishReport);
}

export function navigateToAdmin() {

    return navigateTo(pageUrls.admin);
}

export function navigateToAdminLogin() {

    return navigateTo(pageUrls.adminLogin);
}

export function navigateToPersonalHome() {

    return navigateTo(pageUrls.personal);
}
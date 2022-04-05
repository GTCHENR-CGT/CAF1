import apiUrls from "../../API/apiUrls.js";
import httpInstance from "../../axios/axios.interceptors.js";


export function getReportUrlByNewest() {
    return httpInstance.get(apiUrls.getReportUrlByNewest);
}
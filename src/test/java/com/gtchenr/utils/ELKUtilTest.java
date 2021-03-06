package com.gtchenr.utils;

import com.alibaba.fastjson.JSON;
import com.gtchenr.mapper.ReportMapper;
import com.gtchenr.pojo.Report;
import com.gtchenr.service.ReportService;
import com.gtchenr.service.impl.ReportServiceImpl;
import org.apache.http.HttpHost;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class ELKUtilTest {

    private static String HOSTNAME = "localhost";
    private static Integer PORT = 9200;
    private static RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost(HOSTNAME, PORT, "http")));
    ReportMapper reportMapper = MybatisUtil.getSqlSession1().getMapper(ReportMapper.class);
    @Test
    public void getTest1() throws IOException {
        Book book = new Book("dd", 12, "hhhh");
        ELKUtil.add("book", book);
    }

    @Test
    public void Test02() throws IOException {
        IndexRequest request = new IndexRequest("book");
        request.id("4");
        Book book = new Book("dd", 12, "hhhh");
        String s = JSON.toJSONString(book);

        request.source(s, XContentType.JSON);
        request.timeout(TimeValue.timeValueSeconds(1));
        request.timeout("1s");

        IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);
        String index = indexResponse.getIndex();
        String id = indexResponse.getId();
        //获取插入的类型
        if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
            DocWriteResponse.Result result = indexResponse.getResult();
            System.out.println("CREATED:" + result);
        } else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {
            DocWriteResponse.Result result = indexResponse.getResult();
            System.out.println("UPDATED:" + result);
        }

        ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
        if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
            System.out.println("处理成功的分片数少于总分片！");
        }
        if (shardInfo.getFailed() > 0) {
            for (ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures()) {
                String reason = failure.reason();//处理潜在的失败原因
                System.out.println(reason);
            }
        }
    }

    @Test
    public void TestGet() {
        String book = ELKUtil.get("book", "1");
        System.out.println(book);

    }

    @Test
    public void queryAllTest() {
        List<String> strings = ELKUtil.queryAll("report", 1, 1);
        for (String s : strings) {
            System.out.println(s);
        }
    }

    @Test
    public void queryByIdsTest() {
        List<String> list = ELKUtil.queryByIds("report", "1", "2");
        for (String s : list) {
            System.out.println(s);
        }
    }

    @Test
    public void queryByMultiTest() {
        List<String> list = ELKUtil.queryByMulti("report", 1, 5, "邵晓峰", "reportPeople");
        print(list);
    }


    @Test
    public void queryByMatchTest() {

        List<String> list = ELKUtil.queryByMulti("report", 1, 10, "program director", "reportPeople", "reportPeopleInfo","reportOrganizer");
        print(list);
    }

    @Test
    public void queryByMatchTest1() {

        List<String> list = ELKUtil.queryByMulti("report", 1, 10, "2", "reportId");
        print(list);
    }


    public void print(List<String> list) {
        for (String s : list) {
            System.out.println(s);
        }
    }

    @Test
    public void addTest(){
        List<Report> reports = reportMapper.queryReports();
        for (Report report:reports) {
            System.out.println(report);
            ELKUtil.add("report",report);
        }

    }

    @Test
    public void test03(){
        List<String> list = ELKUtil.queryByMulti("report", 1, 10, "常晋源教授", "reportTitle","reportPeople","reportDetails","reportPeopleInfo","reportOrganizer");
        print(list);
    }
}

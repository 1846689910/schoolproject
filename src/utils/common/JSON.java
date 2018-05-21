package utils.common;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JSON {
    public static void main(String[] args) {
        /**
         * create a JSON node and build JSON
         * */
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        ArrayNode arrayNode = mapper.createArrayNode();
        arrayNode.add("aaa").add("bbb");
        if (! objectNode.has("key1")) objectNode.set("key1", arrayNode);

        /**
         * jackson JsonNode(ObjectNode or ArrayNode) size
         * */
        objectNode.size();
        arrayNode.size();
        /**
         * jackson add a field to ObjectNode
         * */
        String key = "key";
        String value = "value";
        objectNode.put(key, value);  // 添加值, 如果key相同的话，就会将原来的值替换为新值
        objectNode.set(key, arrayNode);  // 添加节点

    }
    public static void parseJsonFile(File jsonFile) throws IOException{
        // 将a.json file读入之后
        FileInputStream fis = new FileInputStream(jsonFile);
        int len;
        StringBuilder sb = new StringBuilder();
        byte[] buf = new byte[1024];
        while ((len = fis.read(buf)) != -1) {
            sb.append(new String(buf, 0, len));
        }
        final String content = sb.toString();
// 解析
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = (ObjectNode) mapper.readTree(content);  // 这个readTree可以直接从文件中读取json, 或从byte[]中读取
// 如果你明确读入的是ArrayNode也可以直接转为(ArrayNode), 就是[], 也可以转成ObjectNode就是{}. 不转的话，默认是JsonNode
// 对于ObjectNode
        ObjectNode n1 =(ObjectNode) node.get("name");
// 对于ArrayNode, 可以遍历获取
        ArrayNode arrayNode = mapper.createArrayNode();
        List<String> list = new ArrayList<>();
        for (JsonNode n : arrayNode) {
            list.add(n.asText());
        }
// 对于最终的数值
        boolean isAdmin = node.get("isAdmin").asBoolean();
        int num = node.get("num").asInt();
        String name = node.get("name").asText();
// 也可以用lambda表达式
        arrayNode.forEach((n) -> list.add(n.asText()));

    }

    @Test
    public void writeJson2File(){
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
        try {
            writer.writeValue(new File("src/tests/lines.json"), mapper.createObjectNode());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static JsonNode readJsonFile(String path){
        try {
            File jsonFile = new File(path);
            FileInputStream fis = new FileInputStream(jsonFile);
            int len;
            StringBuilder sb = new StringBuilder();
            byte[] buf = new byte[1024];
            while ((len = fis.read(buf)) != -1) {
                sb.append(new String(buf, 0, len));
            }
            fis.close();
            final String content = sb.toString();
            return new ObjectMapper().readTree(content);
        } catch(IOException e) {  // The IOException include both FileNotFoundException and fis.read() io exception
            e.printStackTrace();
            return null;
        }
    }
    // 遍历所有次级节点
    public static Iterator<Map.Entry<String, JsonNode>> loopJsonNode(JsonNode node){
        return node.fields();
    }

}

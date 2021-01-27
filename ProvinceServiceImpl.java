import com.alibaba.fastjson.JSON;

import java.util.*;


public class ProvinceServiceImpl {

    static class AreaResource {
        /**
         *  area表示的是地区全路径,最多可能有6级,用分隔符连接,分隔符是 spliter,
         *  例如分隔符是逗号 则area型如  中国,四川,成都    中国,浙江,杭州  中国,浙江,义乌
         *  count表示门店数
         */
        String area;
        String spliter;
        long count;
    }

    static class Node {
        String name;
        Node parent;			// 标记为父节点
        List<Node> nexts;		// 标记是否有下一个节点，如果没有，说明为最后一个节点
        long count;

        /**
         *  如果nexts为null或者size() == 0, 说明为最后的节点，只打印对象的count， 否则交给nexts打印
         */
        Map<String, Object> toJsonString() {
            Map<String, Object> re;
            if (nexts == null || nexts.size() == 0) {
                re = new HashMap<>(1);
            } else {
                re = new HashMap<>(this.nexts.size());
                for (Node node : this.nexts) {
                    Map<String, Object> map = node.toJsonString();
                    if(map.size() > 0) {
                        re.put(node.name, map);
                    } else {
                        re.put(node.name, node.count);
                    }
                }
            }
            return re;
        }
    }

    private String getFormattedJSONByResource(List<AreaResource> areas) {
        if (areas == null || areas.size() == 0) {
            return "{}";
        }

        List<Node> result = new ArrayList<>();  // 可以改为map,更方便
        for (AreaResource ar : areas) {
            String[] arr = ar.area.split(ar.spliter);
            if (arr == null || arr.length == 0) {
                continue;
            }

            Node temp = null;
            for(int i = 0 ; i < arr.length; i++) {
                String area = arr[i];
                if (i == 0) {
                    if (result.size() == 0) {
                        temp = new Node();
                        temp.name = area;
                        temp.count = ar.count;

                        result.add(temp);
                    } else {
                        // 查找是否存在当前节点
                        for (Node node : result) {
                            if (node.name.equals(area)) {
                                temp = node;
                                break;
                            }
                        }

                        if (temp == null) {
                            // 创建新的
                            temp = new Node();
                            temp.name = area;
                            temp.count = ar.count;

                            result.add(temp);
                        } else {
                            // 累计数量
                            temp.count += ar.count;
                        }
                    }
                } else {
                    // temp为上一个节点, 从下级别节点中找
                    if (temp.nexts == null || temp.nexts.size() == 0) {
                        Node subTemp = new Node();
                        subTemp.name = area;
                        subTemp.count = ar.count;

                        temp.nexts = new ArrayList<>(Collections.singletonList(subTemp));

                        Node parent = temp;
                        temp = subTemp;
                        temp.parent = parent;
                    } else {
                        Node subTemp = null;
                        for (Node node : temp.nexts) {
                            if (node.name.equals(area)) {
                                subTemp = node;
                                break;
                            }
                        }

                        if (subTemp == null) {
                            // 创建新的
                            subTemp = new Node();
                            subTemp.name = area;
                            subTemp.count = ar.count;
                            temp.nexts.add(subTemp);
                        } else {
                            // 找到了
                            subTemp.count += ar.count;
                        }

                        Node parent = temp;
                        temp = subTemp;
                        temp.parent = parent;
                    }
                }
            }
        }

        return translate(result);
    }

    private String translate(List<Node> list) {
        Map<String, Object> result = new HashMap<>(list.size());
        for (Node node : list) {
            result.put(node.name, node.toJsonString());
        }
        return JSON.toJSONString(result);
    }

    public static void main(String[] args) {
        ProvinceServiceImpl service = new ProvinceServiceImpl();
        List<AreaResource> areas = new ArrayList<>(4);
        AreaResource areaResource = new AreaResource();
        areaResource.area = "中国,四川,成都,测试";
        areaResource.spliter = ",";
        areaResource.count = 10;
        areas.add(areaResource);

        areaResource = new AreaResource();
        areaResource.area = "中国,四川,德阳,测试";
        areaResource.spliter = ",";
        areaResource.count = 20;
        areas.add(areaResource);

        areaResource = new AreaResource();
        areaResource.area = "中国,浙江,杭州,测试";
        areaResource.spliter = ",";
        areaResource.count = 20;
        areas.add(areaResource);

        areaResource = new AreaResource();
        areaResource.area = "中国,浙江,杭州,测试2";
        areaResource.spliter = ",";
        areaResource.count = 30;
        areas.add(areaResource);

        System.out.println(service.getFormattedJSONByResource(areas));
    }
}

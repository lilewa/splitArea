import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Oho {

    public static StringBuilder sb=new StringBuilder();
    public static void main(String[] args) throws Exception{



       List<AreaResource> areas =new ArrayList<>();
       AreaResource l=new AreaResource("中国,四川,成都",",",10);
       AreaResource l2=new AreaResource("中国,四川,眉山",",",20);
        AreaResource l3=new AreaResource("中国,浙江,义乌",",",22);
       areas.add(l);
       areas.add(l2);
        areas.add(l3);
       String s=getFormattedJSONByResource(areas);
       System.out.println(s);
   }

   public static  String getFormattedJSONByResource(List<AreaResource> areas){

       Node root=new Node("root");
       int[] index=new int[6];

       for(AreaResource areaSource: areas){

            String[] names = areaSource.area.split(areaSource.spliter);
            int i=0;

            for(String name:names){
                Node currentNode=root;
                for(int j=0;j<i;j++){
                    currentNode = currentNode.nodes.get(index[j]);
                }

                if(currentNode.nodes==null)
                    currentNode.nodes=new ArrayList<>();

                Node newNode=new Node(name);
                index[i] = currentNode.nodes.indexOf(newNode);
                if(index[i]==-1){
                    currentNode.nodes.add(newNode);
                    index[i]=currentNode.nodes.size()-1;
                }
                i++;
                if(i==names.length){
                    newNode.nodes=new ArrayList<Node>(){ {add(new Node(String.valueOf(areaSource.count))) ;} };
                }
            }
       }


       sb.append("{");
       System.out.println(sb.toString());
       getString(root.nodes.get(0));
       sb.append("}");
       System.out.println(sb.toString());
       return sb.toString();
   }

    public static void getString( Node node ) {

        if (node.nodes!=null&& node.nodes.get(0).nodes!=null){
            sb.append("\""+ node.name+"\":");
            sb.append("{");
            for(int i=0; i<node.nodes.size(); i++){
                getString(node.nodes.get(i));
                sb.append(",");
            }
            sb.deleteCharAt(sb.length()-1);
            sb.append("}");
        }else{

            if (node.nodes==null){
                sb.append(node.name);

            }else{
                sb.append("\""+ node.name+"\":");
                getString(node.nodes.get(0));
            }
        }

        System.out.println(sb.toString());
    }
}


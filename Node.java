import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Node {
    public String name;
    public List<Node> nodes;
    public Node(String name){
        this.name=name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return name.equals(node.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    Map<String, Object> toJsonString(){

        return  new HashMap<>();
    }
}

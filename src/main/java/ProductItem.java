public class ProductItem {
    public String title = "", type = "";
    public String sDBServer = "", sDBBase = "", sDBUser = "", sDBPsw = "", sQuery = "";

    @Override
    public String toString() {
        return title;
    }
}

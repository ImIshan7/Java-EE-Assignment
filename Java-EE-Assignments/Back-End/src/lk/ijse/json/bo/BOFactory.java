package lk.ijse.json.bo;
import lk.ijse.json.bo.custom.impl.CustomerBOImpl;
import lk.ijse.json.bo.custom.impl.ItemBOImpl;
import lk.ijse.json.bo.custom.impl.OrderDetailBOImpl;
import lk.ijse.json.bo.custom.impl.OrdersBOImpl;

public class BOFactory {


    private static BOFactory boFactory;
    private BOFactory(){
    }

    public static BOFactory getBoFactory(){
        return (boFactory== null) ? boFactory = new BOFactory() : boFactory;
    }

    public enum BoTypes{
        CUSTOMER,ITEM,ORDERS,ORDER_DETAIL
    }


    public  SuperBO getBO(BoTypes types){
        switch (types){

            case CUSTOMER:
                return new CustomerBOImpl();

            case ITEM:
                return new ItemBOImpl();

            case ORDERS:
                return new OrdersBOImpl();

            case ORDER_DETAIL:
                return new OrderDetailBOImpl();

        }
        return null;
    }

}

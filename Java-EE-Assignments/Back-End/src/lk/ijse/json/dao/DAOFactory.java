package lk.ijse.json.dao;


import lk.ijse.json.dao.custom.impl.*;

public class DAOFactory {
    private static DAOFactory daoFactory;
    private DAOFactory(){
    }

    public static DAOFactory getDaoFactory(){
        return (daoFactory== null) ? daoFactory = new DAOFactory() : daoFactory;
    }

    public enum DAOTypes{
        CUSTOMER,ITEM,ORDERS,ORDER_DETAIL,QUERY
    }


    public SuperDAO getDAO(DAOFactory.DAOTypes types){
        switch (types){

            case CUSTOMER:
                return new CustomerDAOImpl();

            case ITEM:
                return new ItemDAOImpl();

            case ORDERS:
                return new OrdersDAOImpl();

            case ORDER_DETAIL:
                return new OrderDetailDAOImpl();

            case QUERY:
                return new QueryDAOImpl();

        }
        return null;
    }
}

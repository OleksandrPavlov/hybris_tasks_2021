package com.learning.pavlov.dao.impl;

import com.learning.pavlov.constants.SQLQueriesConstants;
import com.learning.pavlov.dao.ProductDao;
import com.learning.pavlov.jdbc.handler.HandlerFactory;
import com.learning.pavlov.models.Product;
import com.learning.pavlov.util.ThreadLocalConnection;
import org.apache.commons.dbutils.QueryRunner;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.learning.pavlov.jdbc.handler.JDBCUtil.putErrorMsgToConnection;

public class ProductDaoImpl implements ProductDao {
    private final QueryRunner queryRunner;

    public ProductDaoImpl() {
        queryRunner = new QueryRunner();
    }

    @Override

    public int addProduct(Product product) {
        Connection connection = ThreadLocalConnection.getConnection();
        try {
            return queryRunner.update(connection, SQLQueriesConstants.ADD_PRODUCT,
                    product.getName(),
                    product.getPrice(),
                    product.getProductStatus().toString());
        } catch (SQLException ex) {
            putErrorMsgToConnection(ex.getMessage(), connection);
        }
        return 0;
    }

    @Override
    public List<Product> extractAllOrderedProducts() {
        return getProductList(SQLQueriesConstants.GET_ALL_ORDERED_PRODUCTS);
    }

    @Override
    public List<Product> extractAllProducts() {
        return getProductList(SQLQueriesConstants.GET_ALL_PRODUCTS);
    }

    private List<Product> getProductList(String sqlQuery) {
        Connection connection = ThreadLocalConnection.getConnection();
        try {
            return queryRunner.query(connection, sqlQuery, HandlerFactory.PRODUCT_LIST_HANDLER);
        } catch (SQLException ex) {
            putErrorMsgToConnection(ex.getMessage(), connection);
        }
        return Collections.emptyList();
    }

    @Override
    public int removeProduct(int productId) {
        return update(SQLQueriesConstants.REMOVE_PRODUCT_BY_ID,productId);
    }

    @Override
    public int removeAllProducts() {
        return update(SQLQueriesConstants.DELETE_ALL_PRODUCTS);
    }

    private int update(String query,Object...parameters) {
        Connection connection = ThreadLocalConnection.getConnection();
        try {
            return queryRunner.update(connection, query,parameters);
        } catch (SQLException ex) {
            putErrorMsgToConnection(ex.getMessage(), connection);
        }
        return 0;
    }

    @Override
    public Optional<Product> getProductById(long productId) {
        Connection connection = ThreadLocalConnection.getConnection();
        try {
            return queryRunner.query(connection, SQLQueriesConstants.GET_PRODUCT_BY_ID, HandlerFactory.PRODUCT_RESULT_SET_HANDLER, productId);
        } catch (SQLException ex) {
            putErrorMsgToConnection(ex.getMessage(), connection);
        }
        return Optional.empty();
    }
}

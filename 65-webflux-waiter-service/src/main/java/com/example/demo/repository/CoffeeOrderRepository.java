package com.example.demo.repository;

import com.example.demo.model.Coffee;
import com.example.demo.model.CoffeeOrder;
import com.example.demo.model.OrderState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.function.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

@Repository
public class CoffeeOrderRepository {
    @Autowired
    private DatabaseClient databaseClient;

    /**
     * 通过id查询CoffeeOrder
     * @param id
     * @return
     */
    public Mono<CoffeeOrder> get(Long id) {

        return databaseClient.execute()                             //使用databaseClient execute方法拼装
                .sql("select * from t_order where id = " + id)      //传入Sql语句，参数id
                .map((r, rm) ->                                     //map方式 构建CoffeeOrder对象
                            CoffeeOrder.builder()
                                    .id(id)
                                    .customer(r.get("customer", String.class))
                                    .state(OrderState.values()[r.get("state", Integer.class)])
                                    .createTime(r.get("create_time", Date.class))
                                    .updateTime(r.get("update_time", Date.class))
                                    .items(new ArrayList<Coffee>())
                                    .build()
                )
                .first() //取出第一条记录
                .flatMap(o ->
                        databaseClient.execute()    //对取出的记录进行数据库查询
                                .sql("select c.* from t_coffee c, t_order_coffee oc " +
                                    "where c.id = oc.items_id and oc.coffee_order_id = " + id)
                                .as(Coffee.class)
                                .fetch()
                                .all()
                                .collectList()
                                .flatMap(l -> { //查询结果传给getItems对象
                                    o.getItems().addAll(l);
                                    return Mono.just(o);
                                })
                );//返回Mono对象
    }

    public Mono<Long> save(CoffeeOrder order) {

        return databaseClient.insert().into("t_order")//使用databaseClient inser方法拼装
                .value("customer", order.getCustomer())
                .value("state", order.getState().ordinal())
                .value("create_time", new Timestamp(order.getCreateTime().getTime()))
                .value("update_time", new Timestamp(order.getUpdateTime().getTime()))
                .fetch()
                .first()
                .flatMap(m -> Mono.just((Long) m.get("ID")))
                .flatMap(id -> Flux.fromIterable(order.getItems())
                        .flatMap(c -> databaseClient.insert().into("t_order_coffee")
                                .value("coffee_order_id", id)
                                .value("items_id", c.getId())
                                .then()).then(Mono.just(id)));
    }
}

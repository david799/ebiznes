
import { Form, Button, Row, Col } from 'react-bootstrap';
import { withRouter, useHistory } from 'react-router-dom'
import axios from "axios";
import React, { useState, useEffect } from "react";

export function OrdersList() {
    const history = useHistory();
    const [orders, setOrders] = useState([]);

    const getOrders = async () => {
        try {
            const response = await axios.get(`http://localhost:9000/orders`).then(res => {
                setOrders(res.data)
            });
        } catch (e) {
            console.log(e);
        }
    }

    useEffect(() => {
        getOrders();
    }, [])

    function handleViewOrder(order_id){
        history.push({
            pathname: '/order',
            state: {'order_id': order_id}
        });
    }
    let colouring_index = 0;
    let colours = [{ 'background-color': '#fff' }, { 'background-color': '#e8e8e8' }]
    
    return (
        <>
            <link
                rel="stylesheet"
                href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css"
                integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk"
                crossorigin="anonymous"
            />
            <div className="px-md-5 pt-md-4">
                <h1>Lista zamówień</h1>
                {
                    orders.map((order) => {
                        colouring_index++
                        return (
                            <div>
                                <Row style={colours[colouring_index % 2]} className="my-1" onClick={() => handleViewOrder(order.id)}>
                                    <Col><b>Id:</b> {order.id}</Col>
                                    <Col><b>Imie i nazwisko:</b> {order.address.name}</Col>
                                    <Col><b>Adres linia1:</b> {order.address.address_line1}</Col>
                                    <Col><b>Adres linia2:</b> {order.address.address_line2}</Col>
                                </Row>
                            </div>
                        )
                    }
                    )
                }
            </div>
        </>
    );
}

export default withRouter(OrdersList);

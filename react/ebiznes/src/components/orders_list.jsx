
import { Button, Row, Badge } from 'react-bootstrap';
import { withRouter, useHistory } from 'react-router-dom'
import axios from "axios";
import React, { useState, useEffect } from "react";

export function OrdersList() {
    const history = useHistory();
    const [orders, setOrders] = useState([]);

    const getOrders = async () => {
        try {
            await axios.get(`https://ebiznesbackend.azurewebsites.net/orders`).then(res => {
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
                        return (
                            <div key={"order" + order.id}>
                                <Row className="my-1">
                                    <Badge className="m-1" variant="light"><h5><b>Id:</b> {order.id}</h5></Badge>
                                    <Button onClick={() => handleViewOrder(order.id)}>Zobacz szczegóły</Button>
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

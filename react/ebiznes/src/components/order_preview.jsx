
import { Form, Button, Row, Col } from 'react-bootstrap';
import { withRouter, useLocation, useHistory } from 'react-router-dom'
import axios from "axios";
import React, { useState, useEffect } from "react";

export function OrderPreview() {
    const location = useLocation();
    const history = useHistory();

    const [products, setProducts] = useState([])
    const [name, setName] = useState([])
    const [addressLine1, setAddressLine1] = useState([])
    const [addressLine2, setAddressLine2] = useState([])

    useEffect(() => {
        setProducts(location.state.products);
    }, [location]);

    function handleTakeOrder() {
        let products_ids = products.map((product) => {
            return product.id
        }
        )
        let json = {
            'id': 0,
            'customer': 0,
            'address': 0,
            'name': name,
            'addressLine1': addressLine1,
            'addressLine2': addressLine2,
            'products': products_ids
        }
        axios.post(`http://localhost:9000/addorder`,
            json,
            {
                headers: {
                    'Content-Type': 'application/json'
                }
            }
        ).then(res => {
            history.push({
                pathname: '/orders_list',
            });
        })
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
                <h1>Twoje zamówienie</h1>
                {
                    products.map((product) => {
                        return (
                            <Row className="my-1">
                                <Col><b>Nazwa:</b> {product.name}</Col>
                                <Col><b>Opis:</b> {product.description}</Col>
                                <Col><b>Cena:</b> {Math.round((product.price + Number.EPSILON) * 100) / 100}zł</Col>
                            </Row>
                        )
                    })
                }
                <Form>
                    <Form.Group controlId='customerName'>
                        <Form.Label>Imie i nazwisko</Form.Label>
                        <Form.Control as="textarea" rows={1} type="text" onChange={(e) => { setName(e.target.value); }} />
                    </Form.Group>
                    <Form.Group controlId='customerAddresLine1'>
                        <Form.Label>Adres linia 1</Form.Label>
                        <Form.Control as="textarea" rows={1} type="text" onChange={(e) => { setAddressLine1(e.target.value); }} />
                    </Form.Group>
                    <Form.Group controlId='customerAddresLine2'>
                        <Form.Label>Adres linia 2</Form.Label>
                        <Form.Control as="textarea" rows={1} type="text" onChange={(e) => { setAddressLine2(e.target.value); }} />
                    </Form.Group>
                    <Button onClick={() => handleTakeOrder()}>Zamów</Button>
                </Form>
            </div>
        </>
    );
}

export default withRouter(OrderPreview);


import { Button, Row, Col } from 'react-bootstrap';
import { useHistory, withRouter } from 'react-router-dom'
import axios from "axios";
import React, { useState, useEffect } from "react";

export function ProductsList() {
    const history = useHistory();

    const [categories, setCategories] = useState([]);
    const [products, setProducts] = useState([]);
    const [basket, setBasket] = useState([]);
    const [basketLength, setBasketLength] = useState(0);

    const getCategories = async () => {
        try {
            await axios.get(`https://ebiznesbackend.azurewebsites.net/categories`).then(res => {
                setCategories(res.data)
            });
        } catch (e) {
            console.log(e);
        }
    }

    useEffect(() => {
        getCategories();
    }, [])

    const getProducts = async () => {
        try {
            await axios.get(`https://ebiznesbackend.azurewebsites.net/products`).then(res => {
                setProducts(res.data)
            });
        } catch (e) {
            console.log(e);
        }
    }

    useEffect(() => {
        getProducts();
    }, [])

    function handleAddToOrder(product){
        let basket_temp = basket;
        basket_temp.push(product)
        setBasket(basket_temp)
        setBasketLength(basket.length)
    }

    function handleEmptyBasket(){
        setBasket([])
        setBasketLength(0)
    }

    function handleTakeOrder(){
       history.push({
           pathname: '/order_preview',
           state: { products: basket }
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
                <Row>
                    <Col className="m-0 col-8"></Col>
                    <Col className="m-0 col-2">
                        <h4 style={{ 'display': 'inline' }}>
                            <span style={{ "float": "right" }}>Koszyk: {basketLength}</span>
                        </h4>
                    </Col>
                    <Col className="m-0 col-1">
                        <Button variant="danger" onClick={() => handleEmptyBasket()}>Wyczyść</Button>
                    </Col>
                    <Col className="m-0 col-1">
                        <Button variant="success"  onClick={() => handleTakeOrder()}>Zamow</Button>
                    </Col>
                </Row>

                {
                    categories.map((category) => {
                        return (
                            <div key={"category" + category.id}>
                                <h1>{category.name}</h1>
                                {
                                    products.map((product) => {
                                        if (category.id === product.category) {
                                            colouring_index++
                                            return (
                                                <Row style={colours[colouring_index % 2]} className="my-1" key={"product" + product.id}>
                                                    <Col><b>Nazwa:</b> {product.name}</Col>
                                                    <Col><b>Opis:</b> {product.description}</Col>
                                                    <Col><b>Cena:</b> {Math.round((product.price + Number.EPSILON) * 100) / 100}zł</Col>
                                                    <Button onClick={() => handleAddToOrder(product)}>Dodaj do zamówienia</Button>
                                                </Row>
                                            )
                                        }
                                        else {
                                            return (
                                                <div></div>
                                            )
                                        }
                                    })
                                }
                            </div>
                        )
                    }
                    )
                }
            </div>
        </>
    );
}

export default withRouter(ProductsList);

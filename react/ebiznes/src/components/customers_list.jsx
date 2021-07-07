
import { Row, Col } from 'react-bootstrap';
import { withRouter } from 'react-router-dom'
import axios from "axios";
import React, { useState, useEffect } from "react";

export function CustomerList() {
    const [customers, setCustomers] = useState([]);

    const getCustomers = async () => {
        try {
            await axios.get(`https://ebiznesbackend.azurewebsites.net/addresses`).then(res => {
                setCustomers(res.data)
            });
        } catch (e) {
            console.log(e);
        }
    }

    useEffect(() => {
        getCustomers();
    }, [])

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
                <h1>Lista klientów</h1>
                {
                    customers.map((customer) => {
                        colouring_index++
                        return (
                            <div>
                                <Row style={colours[colouring_index % 2]} className="my-1">
                                    <Col><b>Id klienta:</b> {customer.customer}</Col>
                                    <Col><b>Imie i nazwisko:</b> {customer.name}</Col>
                                    <Col><b>Adres linia1:</b> {customer.address_line1}</Col>
                                    <Col><b>Adres linia2:</b> {customer.address_line2}</Col>
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

export default withRouter(CustomerList);

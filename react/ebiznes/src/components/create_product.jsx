import { Form, Button, Row } from 'react-bootstrap';
import { withRouter } from 'react-router-dom'
import axios from "axios";
import React, { useState } from "react";

export function CreateProduct() {

    const [newCategoryName, setNewCategoryName] = useState('');

    let handleSubmit = event => {
        event.preventDefault();
        AddCategory()
    }

    function AddCategory() {
        let json = {}
        json['name'] = newCategoryName;
        axios.post(`localhost:9001/addcategory`,
            json,
            {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            }
        ).then(res => {
            
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
                <h1>Dodaj nowy produkt</h1>
                <Form>
                    <Row>
                        <Form.Group controlId='newProductName'>
                            <Form.Label>Nazwa produktu</Form.Label>
                            <Form.Control as="textarea" rows={1} type="text" onChange={(e) => { setNewCategoryName(e.target.value); }} />
                        </Form.Group>
                    </Row>
                    <Button variant="primary" type='submit' onClick={handleSubmit}>Dodaj kategorie</Button>
                </Form>
            </div>
        </>
    );
}

export default withRouter(CreateProduct);

import { Form, Button, Row } from 'react-bootstrap';
import { withRouter } from 'react-router-dom'
import axios from "axios";
import React, { useState } from "react";

export function CreateCategory() {

    const [newCategoryName, setNewCategoryName] = useState('');

    let handleSubmit = event => {
        AddCategory()
    }

    function AddCategory() {
        let json = {
            'id': 0,
            'name': newCategoryName
        }
        axios.post(`http://localhost:9000/addcategory`,
            json,
            {
                headers: {
                    'Content-Type': 'application/json'
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
                <h1>Dodaj nową kategorię</h1>
                <Form onSubmit={handleSubmit}>
                    <Form.Group controlId='newCategoryName'>
                        <Form.Label>Nazwa kategorii</Form.Label>
                        <Form.Control as="textarea" rows={1} type="text" onChange={(e) => { setNewCategoryName(e.target.value); }} />
                    </Form.Group>
                    <Button variant="primary" type='submit'>Dodaj kategorie</Button>
                </Form>
            </div>
        </>
    );
}

export default withRouter(CreateCategory);

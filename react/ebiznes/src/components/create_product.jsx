import { Form, Button, Dropdown, DropdownButton, InputGroup } from 'react-bootstrap';
import { withRouter } from 'react-router-dom'
import axios from "axios";
import React, { useState, useEffect } from "react";

export function CreateProduct() {

    const [newProductName, setNewProductName] = useState('');
    const [newProductDescription, setNewProductDescription] = useState('');
    const [newProductCategory, setNewProductCategory] = useState(null);
    const [newProductCategoryName, setNewProductCategoryName] = useState('');
    const [newProductPrice, setNewProductPrice] = useState(0.0);
    const [categories, setCategories] = useState([]);

    const getCategories = async () => {
        try {
            await axios.get(`http://localhost:9000/categories`).then(res => {
                setCategories(res.data)
            });
        } catch (e) {
            console.log(e);
        }
    }

    useEffect(() => {
        getCategories();
    }, [])

    let handleSubmit = event => {
        AddProduct()
    }

    function AddProduct() {
        let json = {
            "id": 0,
            "name": newProductName,
            "description": newProductDescription,
            "category": newProductCategory,
            "price": parseFloat(newProductPrice),
        }
        axios.post(`http://localhost:9000/addproduct`,
            json,
            {
                headers: {
                    'Content-Type': 'application/json'
                }
            }
        )
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
                <Form onSubmit={handleSubmit}>
                    <Form.Group controlId='newProductName'>
                        <Form.Label>Nazwa produktu</Form.Label>
                        <Form.Control as="textarea" rows={1} type="text" onChange={(e) => { setNewProductName(e.target.value); }} />
                    </Form.Group>
                    <Form.Group controlId='newProductDescription'>
                        <Form.Label>Opis</Form.Label>
                        <Form.Control as="textarea" rows={1} type="text" onChange={(e) => { setNewProductDescription(e.target.value); }} />
                    </Form.Group>
                    <Form.Group controlId='newProductPrice'>
                        <Form.Label>Cena</Form.Label>
                        <Form.Control type='number' step="0.01" min='0' max='999999999' onChange={(e) => { setNewProductPrice(e.target.value); }} />
                    </Form.Group>
                    <Form.Group controlId='newProductCategory'>
                        <Form.Label>Nazwa kategorii</Form.Label>
                        <InputGroup className="mb-3">
                            <DropdownButton variant="outline-secondary" title="Kategorie" id="input-group-dropdown-1">
                                {
                                    categories.map((category) => {
                                        return <Dropdown.Item href="#" onSelect={(e) => { setNewProductCategory(category.id); setNewProductCategoryName(category.name) }}>{category.name}</Dropdown.Item>
                                    }
                                    )
                                }
                            </DropdownButton>
                            <Form.Control disabled type="text" value={newProductCategoryName} />
                        </InputGroup>
                    </Form.Group>
                    <Button variant="primary" type='submit'>Dodaj produkt</Button>
                </Form>
            </div>
        </>
    );
}

export default withRouter(CreateProduct);

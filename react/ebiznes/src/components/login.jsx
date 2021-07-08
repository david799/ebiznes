import { Form, Button } from 'react-bootstrap';
import { withRouter } from 'react-router-dom'
import axios from "axios";
import React, { useState } from "react";
import Cookies from "js-cookie";

export function Login() {
    const [email, setEmail] = useState("")
    const [password, setPassword] = useState("")

    function signIn(){
        axios
            .post(
                `https://ebiznesbackend.azurewebsites.net/signIn`,
                {
                    email: email,
                    password: password
                },
                {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }
            )
            .then((response) => {
                if (response.status !== 200) {
                    alert("Nie udało się zalogować!")
                }
                else if (response.status === 200) {
                    Cookies.set("email", response.data.email);
                    Cookies.set("userId", response.data.id);
                    alert("Zalogowano!")
                }
                console.log(response)
            })
            .catch((error) => {
                if (error.status !== 200) {
                    alert("Nie udało się zalogować!")
                }
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
            <Form className="m-3">
                <h1>Zaloguj</h1>
                <Form.Group controlId="formBasicEmail">
                    <Form.Label>Email</Form.Label>
                    <Form.Control type="email"  onChange={(e) => { setEmail(e.target.value) }}/>
                </Form.Group>

                <Form.Group controlId="formBasicPassword">
                    <Form.Label>Hasło</Form.Label>
                    <Form.Control type="password" onChange={(e) => { setPassword(e.target.value) }}/>
                </Form.Group>
                <Button variant="primary" type="submit" onClick={() => signIn()}>
                    Zaloguj
                </Button>
            </Form>
        </>
    );
}

export default withRouter(Login);

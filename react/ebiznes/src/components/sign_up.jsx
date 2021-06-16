import React, { useState } from "react";
import { withRouter } from "react-router-dom";
import { Form, Button, Row } from 'react-bootstrap';

function SignUp({ history }) {
  const [email, setEmail] = useState("");
  const [pass, setPass] = useState("");

  const readEmail = (e) => {
    setEmail(e.target.value);
  };
  const readPass = (e) => {
    setPass(e.target.value);
  };
  const tryLogin = (e) => {
    e.preventDefault();
    setEmail("");
    setPass("");
    //LogIn(login, pass);
  };

  //const LogIn = async (login, pass) => {
    
  //};
  return (
    <>
      <link
        rel="stylesheet"
        href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css"
        integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk"
        crossorigin="anonymous"
      />

      <div>
        <Form onSubmit={tryLogin}>
          <Row className="m-2">
            <Form.Group className="m-2" >
              <Form.Label className="m-2">Email</Form.Label>
              <Form.Control className="m-2" type="text" value={email} onChange={readEmail} />
              <Form.Label className="m-2">Haslo</Form.Label>
              <Form.Control className="m-2" type="password" value={pass} onChange={readPass} />
              <Button className="m-2" variant="primary" type='submit'>Zaloguj</Button>
            </Form.Group>
          </Row>
        </Form>
      </div>
    </>
  );
}
export default withRouter(SignUp);

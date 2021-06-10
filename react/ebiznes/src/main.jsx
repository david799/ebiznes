import React from 'react';
import ProductsList from './components/products_list'
import ProductPreview from './components/product_preview'
import CreateProduct from './components/create_product'
import CreateCategory from './components/create_category'
import OrdersList from './components/orders_list'
import OrderPreview from './components/order_preview'
import OrderProduct from './components/order_product'
import Login from './components/login'
import { Container, Nav, Navbar } from 'react-bootstrap';

import {
  HashRouter as Router,
  Switch,
  Route
} from "react-router-dom";

export function Main() {
  const MyRoute = ({ component: Component, ...rest }) => (
    <Route
      {...rest}
      render={(props) => {
        return <Component {...props} />;
      }}
    />
  );
  return (
    <>
      <Router>
        <Navbar collapseOnSelect expand="sm" bg="dark" variant="dark">
          <Container>
            <Navbar.Brand>
              <img
                alt=""
                src="data:image/png;base64,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"
                width="70"
                height="30"
                className="d-inline-block align-top"
              />{' '}
              </Navbar.Brand>
            <Navbar.Toggle aria-controls="responsive-navbar-nav" />
            <Navbar.Collapse id="responsive-navbar-nav">
              <Nav className="mr-auto">
                <Nav.Item><Nav.Link href="/#/products_list">Lista produktów</Nav.Link></Nav.Item>
                <Nav.Item><Nav.Link href="/#/create_product">Stwórz produkt</Nav.Link></Nav.Item>
                <Nav.Item><Nav.Link href="/#/create_category">Stwórz kategorie</Nav.Link></Nav.Item>
                <Nav.Item><Nav.Link href="/#/orders_list">Lista zamówień</Nav.Link></Nav.Item>
                <Nav.Item><Nav.Link href="/#/wyceny/przegladaj">Wyceny</Nav.Link></Nav.Item>
              </Nav>
            </Navbar.Collapse>
          </Container>
        </Navbar>
        <Switch>
          <MyRoute path="/products_list" component={ProductsList} />
          <MyRoute path="/product_preview/:id">
            <ProductPreview />
          </MyRoute>
          <MyRoute path="/create_product" component={CreateProduct} />
          <MyRoute path="/create_category" component={CreateCategory} />
          <MyRoute path="/orders_list" component={OrdersList} />
          <MyRoute path="/order_preview">
            <OrderPreview />
          </MyRoute>
          <MyRoute path="/order_product" component={OrderProduct} />
          <MyRoute path="/zaloguj" component={Login} />
          <MyRoute component={Login} />
        </Switch>
      </Router>
    </>
  );
}

export default Main;

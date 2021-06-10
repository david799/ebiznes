import React from 'react';
import Main from './main'

import {
  HashRouter as Router,
  Switch,
  Route
} from "react-router-dom";

function App() {
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
        <Switch>
          <MyRoute component={Main} />
        </Switch>
      </Router>
    </>
  );
}

export default App;

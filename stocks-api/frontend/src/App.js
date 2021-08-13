import React, { Component } from 'react';
import './App.css';
import Home from './Home';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import StockList from './StockList';
import StockEdit from './StockEdit';


class App extends Component {
  render() {
    return (
        <Router>
          <Switch>
            <Route path='/' exact={true} component={Home}/>
            <Route path='/stocks' exact={true} component={StockList}/>
			<Route path='/stocks/:id' component={StockEdit}/>
          </Switch>
        </Router>
    )
  }
}


export default App;
import React, { Component } from 'react';
import { Button, ButtonGroup, Container, Table } from 'reactstrap';
import AppNavbar from './AppNavbar';
import { Link } from 'react-router-dom';
import { getConfiguration } from './config';


class StockList extends Component {

    constructor(props) {
        super(props);
        this.state = {stocks: []};
        this.remove = this.remove.bind(this);
    }

    componentDidMount() {
        fetch(getConfiguration().apiUrl + '/api/stocks')
            .then(response => response.json())
            .then(data => this.setState({stocks: data.content}));
    }
	
	async remove(id) {
    await fetch(getConfiguration().apiUrl + `/api/stocks/${id}`, {
        method: 'DELETE',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        }
    }).then(() => {
        let updatedStocks = [...this.state.stocks].filter(i => i.id !== id);
        this.setState({stocks: updatedStocks});
    });
}
	
	render() {
		const {stocks, isLoading} = this.state;
		if (isLoading) {
			return <p>Loading...</p>;
		}
		const stockList = stocks.map(stock => {
			return <tr key={stock.id}>
				<td style={{whiteSpace: 'nowrap'}}>{stock.name}</td>
				<td>{stock.currentPrice}</td>
				<td>{stock.createTime}</td>
				<td>{stock.lastUpdate}</td>
				<td>
					<ButtonGroup>
						<Button size="sm" color="primary" tag={Link} to={"/stocks/" + stock.id}>Edit</Button>
						<Button size="sm" color="danger" onClick={() => this.remove(stock.id)}>Delete</Button>
					</ButtonGroup>
				</td>
			</tr>
    });
    return (
        <div>
            <AppNavbar/>
            <Container fluid>
                <div className="float-right">
                    <Button color="success" tag={Link} to="/stocks/new">Add Stock</Button>
                </div>
                <h3>Stocks</h3>
                <Table className="mt-4">
                    <thead>
                    <tr>
                        <th width="20%">Name</th>
                        <th width="20%">Current Price</th>
						<th width="20%">Creat Time</th>
						<th width="20%">Last Update</th>
                        <th width="40%">Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {stockList}
                    </tbody>
                </Table>
            </Container>
        </div>
    );
  }
}
export default StockList;
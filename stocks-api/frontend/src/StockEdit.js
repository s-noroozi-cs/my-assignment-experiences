import React, { Component } from 'react';
import { Link, withRouter } from 'react-router-dom';
import { Button, Container, Form, FormGroup, Input, Label } from 'reactstrap';
import AppNavbar from './AppNavbar';
import { getConfiguration } from './config';

class StockEdit extends Component {

    emptyItem = {
        name: '',
        currentPrice: ''
    };

    constructor(props) {
        super(props);
        this.state = {
            item: this.emptyItem
        };
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }
	
	async componentDidMount() {
        if (this.props.match.params.id !== 'new') {
            const stock = await (await fetch(getConfiguration().apiUrl + `/api/stocks/${this.props.match.params.id}`)).json();
            this.setState({item: stock});
        }
    }
	
	handleChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;
        let item = {...this.state.item};
        item[name] = value;
        this.setState({item});
    }
	
	async handleSubmit(event) {
		event.preventDefault();
		const {item} = this.state;

		await fetch(getConfiguration().apiUrl + '/api/stocks' + (item.id ? '/' + item.id : ''), {
			method: (item.id) ? 'PUT' : 'POST',
			headers: {
				'Accept': 'application/json',
				'Content-Type': 'application/json'
			},
			body: JSON.stringify(item),
		});
		this.props.history.push('/stocks');
	}
	
	render() {
        const {item} = this.state;
        const title = <h2>{item.id ? 'Edit Stock' : 'Add Stock'}</h2>;

        return <div>
            <AppNavbar/>
            <Container>
                {title}
                <Form onSubmit={this.handleSubmit}>
                    <FormGroup>
                        <Label for="name">Name</Label>
                        <Input type="text" name="name" id="name" value={item.name || ''}
                               onChange={this.handleChange} autoComplete="name"/>
                    </FormGroup>
                    <FormGroup>
                        <Label for="currentPrice">Current Price</Label>
                        <Input type="text" name="currentPrice" id="currentPrice" value={item.currentPrice || ''}
                               onChange={this.handleChange} autoComplete="currentPrice"/>
                    </FormGroup>
                    <FormGroup>
                        <Button color="primary" type="submit">Save</Button>{' '}
                        <Button color="secondary" tag={Link} to="/stocks">Cancel</Button>
                    </FormGroup>
                </Form>
            </Container>
        </div>
    }

}
export default withRouter(StockEdit);
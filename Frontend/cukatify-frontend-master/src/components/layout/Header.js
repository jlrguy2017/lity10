import React, { Component } from "react";

import SearchBar from "./SearchBar";
import {  NavLink } from "react-router-dom";
import "./Header.css";
import { connect } from "react-redux";
import { logout } from "../../actions/securityActions";


class Header extends Component {


    logout = () => {
        console.log(1)
        this.props.logout();
        window.location.href = "/Login";
      }

    render() {
        const { validToken, user } = this.props.security;

        let headerLinks;
        const userIsNotAuthenticated = (
            <NavLink exact to="/login" className = "ui button" style = {{marginLeft: 25}}>
                            Sign In
                            </NavLink>
          );

        const userIsAuthenticated = (
            <NavLink onClick={logout}
            to="/Login" className = "ui button" style = {{marginLeft: 25}}>
                             Logout
                            </NavLink>
          );

        if (validToken && user) {
            headerLinks = userIsAuthenticated;
          } else {
            headerLinks = userIsNotAuthenticated;
          }

        return (
            <div className="ui inverted segment">
                <div className="ui inverted secondary pointing menu">
                    <nav>
                        <ul>
                            <li>
                                <NavLink to="/" exact activeClassName="active-link">
                                    Home
                                </NavLink>
                            </li>
                            <li>
                                <NavLink to="/pictures" exact activeClassName="active-link">Pictures</NavLink>
                            </li>
                            <li>
                                <NavLink to="/time" exact activeClassName="active-link">Time</NavLink>
                            </li>
                        </ul>
                    </nav>
                    <div className="right menu">
                    <div className="item">
                            <SearchBar />
                            {headerLinks}
                        </div>
                </div>
                </div>
            </div>
        );
    }
}

const mapStateToProps = state => ({
    security: state.security
  });
  
  export default connect(
    mapStateToProps,
    { logout }
  )(Header);
  

import React, { Component } from 'react'

import { connect } from 'react-redux'
import { getUsers,toggleUser,makeAdmin} from '../../actions/userActions'

class UserList extends Component {

    componentDidMount(){
        this.props.getUsers()
    }

    toggle = (username) => {
      this.props.toggleUser(username,this.props.history)

    }
    makeAdmin = (username) => {
      this.props.makeAdmin(username,this.props.history)
    }

    componentDidUpdate(prevProps, prevState){
      if(prevProps.match.params.name !== this.props.match.params.name){
        this.props.getUsers()
    }
  }

    

    renderList() {
      return this.props.users.map(user => {
          const systemState = user.active ? "Make Passive" : "Make Active"
          const spotifyState = user.spotifyUser ? "positive" : "error"
          const icon = user.spotifyUser ? <i className="thumbs up outline icon"></i> : <i className="thumbs down outline icon"></i>
          return  <tr key={user.username} className={user.active ? "" : "error"}>
          <td>{user.fullName}</td>
          <td>{user.username}</td>
          <td>{user.age}</td>
          <td className={spotifyState}>
            {icon}
          </td>
          <td>
            <button className="small green ui button" onClick={() => this.toggle(user.username)}>{systemState}</button>
            <button className="small blue ui button" onClick={() => this.makeAdmin(user.username)}>Make Admin</button>
          </td>
        </tr>
      })
  }
 
 render = () => {
    return (
      <div className="ui grid">
      <div className="three wide column">
    </div>
      <div className="eleven wide column">
      <h4 className="ui horizontal divider header basic segment">
      <i className="tag icon"></i>
    Here is users on the cukatify
    </h4>
      <table className="ui celled table">
      <thead>
        <tr>
          <th>Name</th>
          <th>Email</th>
          <th>Age</th>
          <th>Spotify User</th>
          <th>Processes</th>
        </tr>
      </thead>
      <tbody>
         {this.renderList()}
      </tbody>
    </table>
      </div>
      <div className="three wide column">
    </div>
      </div>
    )
}

}



const mapStateToProps = ({ userState }) => {
    return { users: userState.users };
}

export default connect(mapStateToProps, {
    getUsers,toggleUser,makeAdmin,
})(UserList)
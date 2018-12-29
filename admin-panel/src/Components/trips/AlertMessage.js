// import React, { PureComponent } from 'react';
// import Snackbar from '@material-ui/core/Snackbar';
// import Button from '@material-ui/core/Button';

// class AlertMessage extends PureComponent {
//   constructor(props) {
//     super(props);
//     this.state = {
//       open: false,
//       message: ''
//     };
//   }

//   handleRequestClose = () => {
//     this.setState({
//       open: false,
//     });
//   };

//   componentDidMount() {
//     if(this.props.message && this.props.message !== '') {
//       this.setState({
//         open: true,
//         message: this.props.message
//       })
//     } else if (this.props.errMessage && this.props.errMessage !== '') {
//       this.setState({
//         open: true,
//         message: this.props.errMessage
//       })
//     } else {
//       this.setState({
//         open: false,
//         message: ''
//       })
//     }
//   }
//   render() {
//     const { open, message } = this.state;
//     return(
//       <Snackbar
//           open={open}
//           message={message}
//           autoHideDuration={4000}
//           onRequestClose={this.handleRequestClose}
//       />
//     )
//   }
// }

// export default AlertMessage;
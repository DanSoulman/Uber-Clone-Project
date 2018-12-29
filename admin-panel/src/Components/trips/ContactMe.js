// import React, { PureComponent } from 'react';
// import { Container, Row, Col } from 'react-grid-system';
// import CircularProgress from '@material-ui/CORE/CircularProgress';

// import ContactForm from './ContactForm';
// import AlertMessage from './AlertMessage';

// const styles = {
//   section: {
//     paddingTop: "75px",
//     paddingBottom: "3em"
//   },
//   loaderStyle: {
//     zIndex: "999",
//     height: "20em",
//     width: "2em",
//     overflow: "show",
//     margin: "12em auto auto auto",
//     top: 0,
//     left: 0,
//     bottom: 0,
//     right: 0
//   }
// }

// class ContactMe extends PureComponent {
//   constructor(props) {
//     super(props);
//     this.handleContact = this.handleContact.bind(this);
//   }

//   handleContact(data) {
//     this.props.action.sendMail(data);
//   }

//   render() {
//     const { contact } = this.props;
//     console.log(this.props);
//     return(
//       <div style={styles.section}>
//       { contact.loading ? <div style={styles.loaderStyle}><CircularProgress /></div> :
//       <Container>
//         <ContactCustomer onSubmit={this.handleContact}/>
//         <AlertMessage message={contact.message} errMessage={contact.errMessage}/>
//       </Container>}
//       </div>
//     )
//   }
// }

// export default ContactMe;
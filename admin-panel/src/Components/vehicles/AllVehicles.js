import React from 'react';
import { GoogleApiWrapper, InfoWindow, Map, Marker } from 'google-maps-react';
import Spinner from '../layout/Spinner';

class AllVehicles extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      showingInfoWindow: false,
      activeMarker: {},
      selectedPlace: {}
    }
    // binding this to event-handler functions
    this.onMarkerClick = this.onMarkerClick.bind(this);
    this.onMapClick = this.onMapClick.bind(this);
  }
  onMarkerClick = (props, marker, e) => {
    this.setState({
      selectedPlace: props,
      activeMarker: marker,
      showingInfoWindow: true
    });
  }
  onMapClick = (props) => {
    if (this.state.showingInfoWindow) {
      this.setState({
        showingInfoWindow: false,
        activeMarker: null
      });
    }
  }
  render() {
    const style = {
      width: '72.2vw',
      height: '75vh',
      align: 'center'
    }
    console.log(this.props);
    const {vehicle} = this.props.location.state; 
    if(vehicle){
      var image = 'http://icons.iconarchive.com/icons/icons-land/transporter/48/Car-Right-Red-icon.png';
      console.log(vehicle);
      return (
        <Map
          item
          xs = { 12 }
          style = { style }
          google = { this.props.google }
          onClick = { this.onMapClick }
          zoom = { 14 }
          initialCenter = {{ lat: vehicle[0].location._lat, lng: vehicle[0].location._long }}
        >
        {vehicle.map((marker, index) => (
            <Marker
              onClick = { this.onMarkerClick }
              title = { vehicle[index].id }
              icon = {{url: image}}
              position = {{ lat: vehicle[index].location._lat, lng: vehicle[index].location._long }}
              name = { vehicle[index].id }
              />
          )
        )
      }
          <InfoWindow
            marker = { this.state.activeMarker }
            visible = { this.state.showingInfoWindow }
          >
          </InfoWindow>
        </Map>
        );
    }
    else{
        return <Spinner />;
    }
  }
}
export default GoogleApiWrapper({
    api: ("AIzaSyBI_XexW7iE_FJv_1rPg7sADzmXQX5sXKs")
})(AllVehicles)
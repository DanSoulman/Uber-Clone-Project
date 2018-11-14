import React from 'react';
import { GoogleApiWrapper, InfoWindow, Map, Marker } from 'google-maps-react';

class GoogleMapsContainer extends React.Component {
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
    var image = 'http://icons.iconarchive.com/icons/icons-land/transporter/48/Car-Right-Red-icon.png';
    return (
      <Map
        item
        xs = { 12 }
        style = { style }
        google = { this.props.google }
        onClick = { this.onMapClick }
        zoom = { 14 }
        initialCenter = {{ lat: this.props.trips.drop._lat, lng: this.props.trips.drop._long }}
      >
        <Marker
          onClick = { this.onMarkerClick }
          title = { this.props.user.name }
          position = {{ lat: this.props.trips.drop._lat, lng: this.props.trips.drop._long }}
          name = { this.props.user.name }
        />
        <Marker
          onClick = { this.onMarkerClick }
          title = { this.props.vehicle.id }
          icon = {{url: image}}
          position = {{ lat: this.props.vehicle.location._lat, lng: this.props.vehicle.location._long }}
          name = { this.props.vehicle.id }
        />
        <InfoWindow
          marker = { this.state.activeMarker }
          visible = { this.state.showingInfoWindow }
        >
        </InfoWindow>
      </Map>
    );
  }
}
export default GoogleApiWrapper({
    api: ("AIzaSyBI_XexW7iE_FJv_1rPg7sADzmXQX5sXKs")
})(GoogleMapsContainer)
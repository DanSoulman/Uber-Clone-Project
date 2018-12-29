import React from 'react';
import TextField from 'material-ui/TextField';

const renderField = ({ input, label, meta: { touched, error }, ...custom }) => {
  return(
        <TextField hintText={label}
          floatingLabelText={label}
          errorText={touched && error}
          {...input}
          {...custom}
          fullWidth={true}
          autoComplete="off"
        />
)};

export default renderField;
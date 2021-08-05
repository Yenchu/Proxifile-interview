import React, { useState } from "react";
import Container from '@material-ui/core/Container';
import Typography from '@material-ui/core/Typography';
import Grid from '@material-ui/core/Grid';
import CompanySearch from './components/CompanySearch';
import Company from './components/Company';
import './App.css';

const App = (props) => {

  const [companies, setCompanies] = useState([]);

  return (
    <Container maxWidth="md">
      <Grid container spacing={3}>
        <Grid item xs={12}>
          <Typography variant="h4" component="h1" gutterBottom>
            Search Company Example
          </Typography>
        </Grid>
        <Grid item xs={12}>
          <CompanySearch setCompanies={setCompanies} />
        </Grid>
        <Grid item xs={12}>
          <Company companies={companies} />
        </Grid>
      </Grid>
    </Container>
  );
}

export default App;

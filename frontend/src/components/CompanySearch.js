import React, { useState } from "react";
import { makeStyles } from '@material-ui/core/styles';
import Paper from '@material-ui/core/Paper';
import InputBase from '@material-ui/core/InputBase';
import IconButton from '@material-ui/core/IconButton';
import SearchIcon from '@material-ui/icons/Search';

const useStyles = makeStyles((theme) => ({
  root: {
    padding: '2px 4px',
    display: 'flex',
    alignItems: 'center',
    width: 400,
  },
  input: {
    marginLeft: theme.spacing(1),
    flex: 1,
  },
  iconButton: {
    padding: 10,
  },
  divider: {
    height: 28,
    margin: 4,
  },
}));

const CompanySearch = (props) => {

  const classes = useStyles();

  const [companyName, setCompanyName] = useState('');

  const [loading, setLoading] = useState(false);

  const handleSearch = () => {
    setLoading(true);
    searchCompany(companyName);
    setCompanyName("");
    setLoading(false);
  }

  const searchCompany = (companyName) => {
    const url = `http://127.0.0.1:8080/companies/${companyName}`;
    const method = {
      method: 'GET',
      headers: {
        // a tmp token for testing
        'Authorization': 'faketoken'
      },
    };

    fetch(url, method)
      .then(res => res.json())
      .then(
        result => {
          props.setCompanies(result);
        },
        error => {
          console.log('search company failed: ' + error)
        }
      ).catch(err => {
        console.log('search company error: ' + err);
      })
  }

  return (
    <Paper className={classes.root}>
      <InputBase
        className={classes.input}
        placeholder="Search Company"
        inputProps={{ 'aria-label': 'search company' }}
        autoFocus
        value={companyName}
        onChange={(e) => setCompanyName(e.target.value)}
      />
      <IconButton className={classes.iconButton} aria-label="search"
        onClick={handleSearch} disabled={loading}>
        <SearchIcon />
      </IconButton>
    </Paper>
  )
}
export default CompanySearch
import React from "react";
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableContainer from '@material-ui/core/TableContainer';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Paper from '@material-ui/core/Paper';
import { makeStyles } from '@material-ui/core/styles';

const useStyles = makeStyles({
  table: {
    minWidth: 650,
  },
});

const Company = (props) => {

  const classes = useStyles();

  const companies = props.companies;

  return (
    <TableContainer component={Paper}>
      <Table className={classes.table} aria-label="company table">
        <TableHead>
          <TableRow>
            <TableCell>Creditor Name</TableCell>
            <TableCell>Address1</TableCell>
            <TableCell>Address2</TableCell>
            <TableCell>City</TableCell>
            <TableCell>State</TableCell>
            <TableCell>Zip</TableCell>
            <TableCell>Attention</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {companies.map((company) => (
            <TableRow key={`${company.name}-${company.address1}`}>
              <TableCell component="th" scope="row">
                {company.name}
              </TableCell>
              <TableCell>{company.address1}</TableCell>
              <TableCell>{company.address2}</TableCell>
              <TableCell>{company.city}</TableCell>
              <TableCell>{company.state}</TableCell>
              <TableCell>{company.zip}</TableCell>
              <TableCell>{company.attention}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  )
}
export default Company
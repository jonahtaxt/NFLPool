import { useEffect, useState } from 'react'
import axios from "axios";
import './App.css';
import "./fonts/Delirium.ttf";
import spinningFootball from './assets/football.gif';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Select from '@mui/material/Select';
import Button from '@mui/material/Button';
import RefreshIcon from '@mui/icons-material/Refresh';
import { grey } from '@mui/material/colors';
import { styled } from '@mui/material/styles';
import { Modal, Typography } from '@mui/material';
import Box from '@mui/material/Box';

function App() {
    const [gameScores, setGameScores] = useState([]);
    const [participants, setParticipants] = useState([]);
    const [orderedParticipants, setOrderedParticipants] = useState([]);
    const [year, setYear] = useState(0);
    const [week, setWeek] = useState(0);
    const [participantId, setParticipant] = useState('');
    const [showPool, setShowPool] = useState(false);
    const [firstRun, setFirstRun] = useState(true);
    const [winner, setWinner] = useState(null);
    const [selectedParticipant, setSelectedParticipant] = useState({});
    const [open, setOpen] = useState(false);
    const handleClose = () => setOpen(false);

    const style = {
        position: 'absolute',
        top: '50%',
        left: '50%',
        transform: 'translate(-50%, -50%)',
        width: 400,
        bgcolor: 'background.paper',
        border: '2px solid #000',
        boxShadow: 24,
        p: 4,
      };

    const nflAPIClient = axios.create({
        baseURL: import.meta.env.VITE_API_URL + "/scores"
    });

    const handleYearChange = (event) => {
        setYear(event.target.value);
    };

    const handleWeekChange = (event) => {
        setWeek(event.target.value);
    }

    const refreshPool = () => {
        fetchPoolData();
    }

    const searchParticipant = (event) => {
        let participant = participants.find(p => p.id == event.target.value);
        if(participant) {
            setSelectedParticipant(participant);
            setOpen(true);
        }
    }

    const ColorButton = styled(Button)(({ theme }) => ({
        color: theme.palette.getContrastText(grey[500]),
        backgroundColor: grey[300],
        '&:hover': {
            backgroundColor: grey[500],
        },
    }));

    let idCounter = 0;
    let poolPosition = 0;

    const fetchPoolData = async () => {
        setFirstRun(false);
        setWinner(null);
        setGameScores([]);
        setParticipants([]);
        setOrderedParticipants([]);
        setParticipant("");
        setShowPool(false);
        let response = await nflAPIClient.get('/' + year + '/' + week);
        if (response.data.gameScores !== null) {
            setGameScores(response.data.gameScores);
            setShowPool(true);
        }
        if (response.data.participants !== null) {
            setParticipants(response.data.participants);
            setOrderedParticipants([...response.data.participants].sort((a, b) => a.name.localeCompare(b.name)));
        }
        if (response.data.participantWinnerId !== null && response.data.participants !== null) {
            setWinner(response.data.participants.filter(participant => {
                return participant.id === response.data.participantWinnerId;
            })[0]);
        }
    }

    useEffect(() => {
        try {
            if(week !== 0 && year !== 0) {
                fetchPoolData();
            }
        } catch (error) {
            console.log(error);
        }
    }, [year, week]);

    return (
        <>
            <h1>La Quiniela</h1>
            <div className="controls">
                <FormControl variant="filled" sx={{ m: 1, minWidth: 120 }}>
                    <InputLabel id="selectedYear">A&ntilde;o</InputLabel>
                    <Select
                        labelId="selectedYear"
                        id="selectedYear"
                        value={year}
                        label="A�o"
                        onChange={handleYearChange}
                    >
                        <MenuItem value={0}></MenuItem>
                        <MenuItem value={2024}>2024</MenuItem>
                    </Select>
                </FormControl>
                <FormControl variant="filled" sx={{ m: 1, minWidth: 120 }}>
                    <InputLabel id="selectedWeek">Semana</InputLabel>
                    <Select
                        labelId="selectedWeek"
                        id="selectedWeek"
                        value={week}
                        label="Age"
                        onChange={handleWeekChange}
                    >
                        <MenuItem value={0}></MenuItem>
                        <MenuItem value={1}>1</MenuItem>
                        <MenuItem value={2}>2</MenuItem>
                        <MenuItem value={3}>3</MenuItem>
                        <MenuItem value={4}>4</MenuItem>
                        <MenuItem value={5}>5</MenuItem>
                        <MenuItem value={6}>6</MenuItem>
                        <MenuItem value={7}>7</MenuItem>
                        <MenuItem value={8}>8</MenuItem>
                        <MenuItem value={9}>9</MenuItem>
                        <MenuItem value={10}>10</MenuItem>
                        <MenuItem value={11}>11</MenuItem>
                        <MenuItem value={12}>12</MenuItem>
                        <MenuItem value={13}>13</MenuItem>
                        <MenuItem value={14}>14</MenuItem>
                        <MenuItem value={15}>15</MenuItem>
                        <MenuItem value={16}>16</MenuItem>
                        <MenuItem value={17}>17</MenuItem>
                        <MenuItem value={18}>18</MenuItem>
                    </Select>
                </FormControl>
                <FormControl variant='filled' sx={{ m: 1, minWidth: 120 }}>
                    <InputLabel id="selectedParticipant">Participante</InputLabel>
                    <Select
                        labelId="selectedParticipant"
                        id="selectedParticipant"
                        value={participantId}
                        label="Participante"
                        onChange={searchParticipant}>
                            {orderedParticipants.map((participant) => {
                                return(
                                    <MenuItem key={participant.id} value={participant.id}>{participant.name}</MenuItem>
                                );
                            })}
                    </Select>
                </FormControl>
                <FormControl>
                    <ColorButton
                        sx={{
                            marginTop: 3,
                            marginLeft: 1
                        }}
                        onClick={refreshPool}
                        variant="contained"
                        disabled={!showPool}
                        startIcon={<RefreshIcon />}
                    >
                        Actualizar</ColorButton>
                </FormControl>
                {showPool && winner !== null &&
                    <FormControl>
                        <div className="winnerParticipant">
                            <div>Ganador: {winner.name}</div>
                            <div>Puntos: {winner.totalPoints}</div>
                            <div>Dif. Monday Night: {winner.mondayNightPointsDifference}</div>
                            <div>Quiniela enviada: {winner.id}</div>
                        </div>
                    </FormControl>
                }
                <Modal
                    open={open}
                    onClose={handleClose}
                    aria-labelledby="modal-modal-title"
                    aria-describedby="modal-modal-description"
                    >
                        <Box sx={style}>
                            <Typography id="modal-modal-title" variant="h6" component="h2">{selectedParticipant.name}</Typography>
                            <Typography id="modal-modal-description" sx={{mt: 2}}>
                                <Table>
                                    <TableRow>
                                        <TableCell>Posici&oacute;n</TableCell>
                                        <TableCell>{selectedParticipant.poolPosition}</TableCell>
                                    </TableRow>
                                    <TableRow>
                                        <TableCell>Aciertos</TableCell>
                                        <TableCell>{selectedParticipant.totalPoints}</TableCell>
                                    </TableRow>
                                    <TableRow>
                                        <TableCell>Diferencia Monday Night</TableCell>
                                        <TableCell>{selectedParticipant.mondayNightPointsDifference}</TableCell>
                                    </TableRow>
                                    <TableRow>
                                        <TableCell>Quiniela Entregada:</TableCell>
                                        <TableCell>{selectedParticipant.id}</TableCell>
                                    </TableRow>
                                </Table>
                            </Typography>
                        </Box>
                </Modal>
            </div>
            {firstRun && <div className="firstRun">Selecciona un a&ntilde;o y semana para ver los resultados</div>}
            {!showPool && !firstRun && <div className="loading"><img src={spinningFootball} alt="loading..." /></div>}
            {showPool && !firstRun &&
                <Paper sx={{ width: '100%', overflow: 'hidden' }} elevation={12}>
                    <TableContainer sx={{ maxHeight: 750 }}>
                        <Table stickyHeader aria-label="sticky table">
                            <TableHead id="tHeader">
                                <TableRow>
                                    <TableCell><div className="points">Posici&oacute;n</div></TableCell>
                                    <TableCell><div className="points" style={{ width: '50%'}}>Nombre</div></TableCell>
                                    <TableCell>
                                        <div className="points" style={{ fontWeight: 'bold' }}>Aciertos</div>
                                    </TableCell>
                                    <TableCell>
                                        <div className="points" style={{ fontWeight: 'bold' }}>Monday Night</div>
                                    </TableCell>
                                    <TableCell>
                                        <div className="points" style={{ fontWeight: 'bold' }}>Dif. Puntos</div>
                                    </TableCell>
                                    {gameScores.map((gameScore) => {
                                        return (
                                            <TableCell key={gameScore.gameScoreId}>
                                                <div className="game">
                                                    <div className={`team ${gameScore.homeScore > gameScore.awayScore ? `winner` : ``}`}>{gameScore.homeTeam.name} ({gameScore.homeScore})</div>
                                                    <div className={`team ${gameScore.homeScore < gameScore.awayScore ? `winner` : ``}`}>{gameScore.awayTeam.name} ({gameScore.awayScore}) @</div>
                                                </div>
                                            </TableCell>
                                        );
                                    })}
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {participants.map((participant) => {
                                    poolPosition++;
                                    participant.poolPosition = poolPosition;
                                    return (
                                        <TableRow key={participant.id}
                                                  sx={{ '&:last-child td, &:last-child th': { border: 0 } }}>
                                            <TableCell><div className="poolPosition">{poolPosition}</div></TableCell>
                                            <TableCell><div className="participantName">{participant.name}</div></TableCell>
                                            <TableCell className={`totalPoints`}>{participant.totalPoints}</TableCell>
                                            <TableCell className={`totalPoints`}>{participant.mondayNightPoints}</TableCell>
                                            <TableCell className={`totalPoints`}>{participant.mondayNightPointsDifference}</TableCell>
                                            {participant.bets.map((bet) => {
                                                let betOnTeam;
                                                let gs;
                                                gs = gameScores.find(gameTeam => gameTeam.awayTeam.name.trim().toUpperCase() === bet.trim());
                                                if (gs === undefined) {
                                                    gs = gameScores.find(gameTeam => gameTeam.homeTeam.name.trim().toUpperCase() === bet.trim());
                                                    if (gs !== undefined) {
                                                        betOnTeam = gs.homeTeam;
                                                    }
                                                } else {
                                                    betOnTeam = gs.awayTeam;
                                                }
                                                idCounter++;
                                                if (betOnTeam !== undefined) {
                                                    return (<TableCell key={idCounter}>
                                                        <img
                                                            src={betOnTeam.logo}
                                                            alt={betOnTeam.name.toUpperCase()}
                                                            title={betOnTeam.name.toUpperCase()}
                                                            className={`logo ${(
                                                                (gs.winningTeam !== null && gs.winningTeam) !== betOnTeam.name)
                                                            && (gs.awayScore > 0 && gs.homeScore > 0)? `loser` : ``}`} />
                                                    </TableCell>);
                                                } else {
                                                    return (<TableCell key={idCounter}>&nbsp;</TableCell>);
                                                }
                                            })}
                                        </TableRow>
                                    );
                                })}
                            </TableBody>
                        </Table>
                    </TableContainer>
                </Paper>
            }
        </>
    );
}

export default App

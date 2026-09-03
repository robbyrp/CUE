// noinspection JSAnnotator

import React, { useState } from 'react';
import styles from './createPerformancePage.module.scss'
import type PerformancePortal from '../../types/Performance';
import { PerformanceService } from '../../services/ReviewService';

function CreatePerformancePage() {
    const [performanceDTO, setPerformanceDTO] = useState<PerformancePortal>
        ({
            title: "",
            director: "",
            coverImageURL: "",
            ageLimit: 0,
            duration: 0,
            location: "",
            startDateTime: "",
            fullCoverImageURL: "",
            purchaseTicketLink: "",
            theaterName: "",
            description: "",
            credits: [],
            reviews: []
        });

    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value, valueAsNumber } = event.currentTarget;
        if (name === 'ageLimit' || name === 'duration') {
            setPerformanceDTO({
                ...performanceDTO,
                [name]: Number.isNaN(valueAsNumber) ? 0 : valueAsNumber
            });
            return;
        }
        setPerformanceDTO({
            ...performanceDTO,
            [name]: value
        });
    };

    const handleSubmit = async (event: React.ChangeEvent<HTMLFormElement>) => {
        event.preventDefault();

        if (performanceDTO.title.trim() === '') {
            alert('Titlul spectacolului este obligatoriu');
            return;
        }

        if (performanceDTO.director.trim() === '') {
            alert('Regizorul spectacolului este obligatoriu');
            return;
        }

        if (performanceDTO.coverImageURL.trim() === '') {
            alert('URL-ul imaginii de coperta este obligatoriu');
            return;
        }

        if (performanceDTO.ageLimit < 1 || performanceDTO.ageLimit > 22) {
            alert("Limita de varsta a spectacolului este un numar intre 1-22");
            return;
        }

        if (performanceDTO.duration < 1 || performanceDTO.duration > 481) {
            alert("Durata e un numar intre 1 si 480 de minute");
            return;
        }

        if (performanceDTO.location.trim() === '') {
            alert('Locatia spectacolului este obligatorie');
            return;
        }

        if (performanceDTO.startDateTime.trim() === '') {
            alert('Data spectacolului este obligatorie');
            return;
        }

        if (performanceDTO.fullCoverImageURL.trim() === '') {
            alert('URL-ul imaginii complete este obligatoriu');
            return;
        }

        if (performanceDTO.purchaseTicketLink.trim() === '') {
            alert('Link-ul pentru bilete este obligatoriu');
            return;
        }

        if (performanceDTO.theaterName.trim() === '') {
            alert('Numele teatrului este obligatoriu');
            return;
        }

        if (performanceDTO.description.trim() === '') {
            alert('Descrierea spectacolului este obligatorie');
            return;
        }

        //-------------------------VERIFICATIONS DONE---------------------------
        try {
            // Mapping - formatting object in order to be handled by backend
            const formattedDate = new Date(performanceDTO.startDateTime!).toISOString();
            const payload = {
                ...performanceDTO,
                startDateTime: formattedDate,
                reviews: [],
                credits: [{
                    role: 'Regizor',
                    names: [performanceDTO.director || 'Necunoscut']
                }]

            };
            await PerformanceService.createPerformance(payload as PerformancePortal);
            alert("Spectacol creat cu succes!");
            //TODO: CREATE ENDPOINT THAT RETURNS ID WHEN PERFORMANCE IS SUCCESSFULLY CREATED
            // SO THAT THE USER CAN BE REDIRECTED TO THE PERFORMANCE HE JUST CREATED
        } catch (error) {
            console.error("Eroare la salvare" + error);
            alert("A aparut o eroare la salvare");
        }

    }


    return (
        <div className={styles.PageContainer}>
            <form className={styles.FormContainer} onSubmit={handleSubmit}>
                <h1>
                    Adauga spectacol
                </h1>

                <div className={styles.FormGroup}>
                    <label htmlFor='title'>Titlu</label>
                    <input
                        type="text"
                        id="title"
                        name="title"
                        value={performanceDTO.title}
                        onChange={handleChange}
                        placeholder="Introdu titlul"
                    />
                </div>

                <div className={styles.FormGroup}>
                    <label htmlFor='director'>Regizor</label>
                    <input
                        type="text"
                        id="director"
                        name="director"
                        value={performanceDTO.director}
                        onChange={handleChange}
                        placeholder="Introdu regizorul"
                    />
                </div>

                <div className={styles.FormGroup}>
                    <label htmlFor='coverImageURL'>URL imagine</label>
                    <input
                        type="url"
                        id="coverImageURL"
                        name="coverImageURL"
                        value={performanceDTO.coverImageURL}
                        onChange={handleChange}
                        placeholder="Introdu link-ul"
                    />
                </div>

                <div className={styles.FormGroup}>
                    <label htmlFor='ageLimit'>Limita de varsta</label>
                    <input
                        type="number"
                        id="ageLimit"
                        name="ageLimit"
                        value={performanceDTO.ageLimit === 0 ? '' : performanceDTO.ageLimit}
                        onChange={handleChange}
                        placeholder="Introdu varsta minima"
                    />
                </div>

                <div className={styles.FormGroup}>
                    <label htmlFor='duration'>Durata spectacolului</label>
                    <input
                        type="number"
                        id="duration"
                        name="duration"
                        value={performanceDTO.duration === 0 ? '' : performanceDTO.duration}
                        onChange={handleChange}
                        placeholder="Introdu durata in minute"
                    />
                </div>

                <div className={styles.FormGroup}>
                    <label htmlFor='location'>Locatie</label>
                    <input
                        type="text"
                        id="location"
                        name="location"
                        value={performanceDTO.location}
                        onChange={handleChange}
                        placeholder="Introdu locatia"
                    //TODO MAKE A CALL TO AN API FOR A GOOGLE LOCATION OR SMTH
                    />
                </div>

                <div className={styles.FormGroup}>
                    <label htmlFor='startDateTime'>Data spectacolului</label>
                    <input
                        type="datetime-local"
                        id="startDateTime"
                        name="startDateTime"
                        value={performanceDTO.startDateTime}
                        onChange={handleChange}
                        placeholder="Data in formatul YYYY-MM-DDThh:mm"
                    />
                </div>

                <div className={styles.FormGroup}>
                    <label htmlFor='fullCoverImageURL'>URL imagine completa</label>
                    <input
                        type="url"
                        id="fullCoverImageURL"
                        name="fullCoverImageURL"
                        value={performanceDTO.fullCoverImageURL}
                        onChange={handleChange}
                        placeholder="Introdu link-ul complet"
                    />
                </div>

                <div className={styles.FormGroup}>
                    <label htmlFor='purchaseTicketLink'>Link bilete</label>
                    <input
                        type="url"
                        id="purchaseTicketLink"
                        name="purchaseTicketLink"
                        value={performanceDTO.purchaseTicketLink}
                        onChange={handleChange}
                        placeholder="Introdu link-ul pentru bilete"
                    />
                </div>

                <div className={styles.FormGroup}>
                    <label htmlFor='theaterName'>Numele teatrului</label>
                    <input
                        type="text"
                        id="theaterName"
                        name="theaterName"
                        value={performanceDTO.theaterName}
                        onChange={handleChange}
                        placeholder="Introdu numele teatrului"
                    />
                </div>

                <div className={styles.FormGroup}>
                    <label htmlFor='description'>Descriere</label>
                    <input
                        type="text"
                        id="description"
                        name="description"
                        value={performanceDTO.description}
                        onChange={handleChange}
                        placeholder="Introdu descrierea"
                    />
                </div>

                <button className={styles.button} type="submit">Salveaza</button>

            </form>

        </div>
    )
}

export default CreatePerformancePage;
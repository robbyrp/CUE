import styles from "./DesktopHeader.module.scss";
import LogoIcon from "./assets/logoIcon.svg";
import homeIcon from "./assets/whiteHouseIcon.svg";
import exploreIcon from "./assets/whiteStarIcon.svg";
import calendarIcon from "./assets/whiteCalendarIcon.svg";
import profileIcon from "./assets/whiteProfileIcon.svg";
import type {SearchSuggestion} from '../../../types/SearchSuggestion';
import { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import { PerformancePortalService } from "../../../services/PerformancePortalService";


function DesktopHeader() {
  const [searchValue, setSearchValue] = useState("");
  const [suggestions, setSuggestions] = useState<SearchSuggestion[]>([]);
    const navigate = useNavigate();
  const showSearchDropdown = searchValue.trim().length > 0;

  useEffect(() => {
    const timer = setTimeout(async () => {
        if (searchValue.trim().length > 0) {
            try {
              const results = await PerformancePortalService.getSearchTitleSuggestions(searchValue);
              setSuggestions(results);
            } catch (e) {
              console.error("Error bringing suggestions", e);
            }
        } else {
            setSuggestions([]);
        }
    }, 300); // 300 ms

    return () => clearTimeout(timer);
    
}, [searchValue]);

  return (
    <div className={styles.header}>
      <div className={styles.logo}>
        <img src={LogoIcon} alt="logo" />
      </div>
      <div className={styles.navLinks}>
        <div className={styles.searchContainer}>
          <div className={styles.searchInputRow}>
            <span className={styles.searchIcon} aria-hidden="true" />
            <input
              type="text"
              placeholder="Search"
              value={searchValue}
              onChange={(event) => setSearchValue(event.target.value)}
            />
          </div>
          {showSearchDropdown && (
            <div className={styles.searchDropdown}>
              {suggestions.map((row: SearchSuggestion) => (
               <div key={row.id} className={styles.searchOption} 
               onClick={() => navigate(`/spectacole/${row.id}`)} style={{cursor: 'pointer'}}>
                  {row.title.toLowerCase()}
                </div> 
              ))
              }
            </div>
          )}
        </div>
        <Link to="/">
          <div className={styles.route}>
            <div className={styles.routeIcon}>
              <img src={homeIcon} alt="home" />
            </div>
            <div className={styles.routeLabel}>
              <span>HOME</span>
            </div>
          </div>
        </Link>
        <Link to="/">
          <div className={styles.route}>
            <div className={styles.routeIcon}>
              <img src={exploreIcon} alt="explore" />
            </div>
            <div className={styles.routeLabel}>
              <span>EXPLORE</span>
            </div>
          </div>
        </Link>
        <a href="/spectacole/adauga">
          <div className={styles.route}>
            <div className={styles.routeIcon}>
              <img src={calendarIcon} alt="calendar" />
            </div>
            <div className={styles.routeLabel}>
              <span>ADAUGA</span>
            </div>
          </div>
        </a>
        <Link to="/login">
          <div className={styles.route}>
            <div className={styles.routeIcon}>
              <img src={profileIcon} alt="profile" />
            </div>
            <div className={styles.routeLabel}>
              <span>PROFILE</span>
            </div>
          </div>
        </Link>
      </div>
    </div>
  );
}

export default DesktopHeader;

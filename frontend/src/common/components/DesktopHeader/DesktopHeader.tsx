import styles from "./DesktopHeader.module.scss";
import LogoIcon from "./assets/logoIcon.svg";
import exploreIcon from "./assets/whiteStarIcon.svg";
import calendarIcon from "./assets/whiteCalendarIcon.svg";
import profileIcon from "./assets/whiteProfileIcon.svg";
import arrowDownIcon from "./assets/whiteArrowDownIcon.svg";
import type { SearchSuggestion } from '../../../types/SearchSuggestion';
import { useState, useEffect, useRef } from "react";
import { Link, useNavigate } from "react-router-dom";
import { SearchService } from "../../../services/SearchService";
import { useAuth } from "../../../auth/AuthContext";
import { ROUTES } from "../../../utils/constants";

function DesktopHeader() {
  const {isAuthenticated, logout} = useAuth();
  const [isDropdownOpen, setIsDropdownOpen] = useState(false);
  const [searchValue, setSearchValue] = useState("");
  const [suggestions, setSuggestions] = useState<SearchSuggestion[]>([]);
  const dropdownRef = useRef<HTMLDivElement>(null);
  const navigate = useNavigate();
  const showSearchDropdown = searchValue.trim().length > 0;

  useEffect(() => {
    const timer = setTimeout(async () => {
      if (searchValue.trim().length > 0) {
        try {
          const results = await SearchService.getSearchTitleSuggestions(searchValue);
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

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target as Node)) {
        setIsDropdownOpen(false);
      }
    };

    if (isDropdownOpen) {
      document.addEventListener("mousedown", handleClickOutside);
    }
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, [isDropdownOpen]);

  const toggleDropdown = () => {
    setIsDropdownOpen((prev) => !prev);
  };

  const handleLogout = () => {
    logout();
    setIsDropdownOpen(false);
    navigate(ROUTES.HOME);
  };


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
              id="text"
              placeholder="Search"
              value={searchValue}
              onChange={(event) => setSearchValue(event.target.value)}
            />
          </div>
          {showSearchDropdown && (
            <div className={styles.searchDropdown}>
              {suggestions.map((row: SearchSuggestion) => (
                <div key={row.id} className={styles.searchOption}
                  onClick={() => navigate(`/spectacole/${row.id}`)} style={{ cursor: 'pointer' }}>
                  {row.title.toLowerCase()}
                </div>
              ))
              }
            </div>
          )}
        </div>
        <Link to={ROUTES.HOME}>
          <div className={styles.route}>
            <div className={styles.routeIcon}>
              <img src={exploreIcon} alt="explore" />
            </div>
            <div className={styles.routeLabel}>
              <span>EXPLOREAZA</span>
            </div>
          </div>
        </Link>
        <Link to={ROUTES.CREEAZA_SPECTACOL}>
          <div className={styles.route}>
            <div className={styles.routeIcon}>
              <img src={calendarIcon} alt="calendar" />
            </div>
            <div className={styles.routeLabel}>
              <span>ADAUGA</span>
            </div>
          </div>
        </Link>
        {isAuthenticated ? (
          <div className={styles.profileContainer} ref={dropdownRef}>
            <Link to={ROUTES.PROFIL} className={styles.profileLink}>
              <div className={styles.route}>
                <div className={styles.routeIcon}>
                  <img src={profileIcon} alt="profile" />
                </div>
                <div className={styles.routeLabel}>
                  <span>CONTUL MEU</span>
                </div>
              </div>
            </Link>

            <button
              type="button"
              className={`${styles.dropdownArrowButton} ${isDropdownOpen ? styles.arrowOpen : ''}`}
              onClick={toggleDropdown}
              aria-label="Deschide meniul contului"
            >
              <img src={arrowDownIcon} alt="arrow" />
            </button>

            {isDropdownOpen && (
              <div className={styles.dropdownMenu}>
                <Link
                  to={ROUTES.PROFIL}
                  onClick={() => setIsDropdownOpen(false)}
                >
                  <div className={styles.dropdownItem}>Activitatea Mea</div>
                </Link>
                <div className={styles.dropdownItem} onClick={handleLogout}>
                  Log Out
                </div>
              </div>
            )}
          </div>
        ) : (
          <Link to={ROUTES.LOGIN}>
            <div className={styles.route}>
              <div className={styles.routeIcon}>
                <img src={profileIcon} alt="login" />
              </div>
              <div className={styles.routeLabel}>
                <span>LOGIN</span>
              </div>
            </div>
          </Link>
        )}
      </div>
    </div>
  );
}

export default DesktopHeader;

# Stock Trading Simulator

A Java-based portfolio management and backtesting system that simulates multiple trading strategies against historical stock/ETF data and compares performance against the S&P 500 benchmark.

## Features

- **Multiple Trading Strategies**
  - Basic Buy-Hold strategy with threshold-based entries
  - Momentum Trading Pattern (trend-following)
  - Breakout Trading Pattern (price breakout detection)
  
- **Portfolio Management**
  - Dynamic position sizing based on risk tolerance
  - Stop-loss and profit-target execution
  - Cash management and budget tracking
  - Per-stock performance analytics

- **Performance Analysis**
  - Real-time portfolio valuation
  - S&P 500 benchmark comparison
  - Individual stock performance ranking
  - Profit/loss tracking (realized and unrealized)

- **Data Processing**
  - CSV data ingestion for stocks, ETFs, and S&P 500 index
  - Date-based price lookups
  - Historical backtesting

## Project Structure

```
stock-simulator/
├── src/project/
│   ├── common/                # Core data models
│   │   ├── Portfolio.java
│   │   ├── StockETF.java
│   │   ├── Investment.java
│   │   ├── PriceDate.java
│   │   ├── MyDate.java
│   │   ├── Security.java
│   │   ├── SAndPIndex.java
│   │   ├── DateResultMap.java
│   │   └── DateCounter.java
│   ├── processor/             # Trading logic & analysis
│   │   ├── TradingStrategy.java
│   │   ├── TradingPattern.java
│   │   ├── MomentumTradingPattern.java
│   │   ├── BreakoutTradingPattern.java
│   │   ├── SortBestWorstPerformers.java
│   │   ├── CalculatePerStockStats.java
│   │   ├── CompareWithSP500.java
│   │   └── TotalValueOnDate.java
│   ├── data/                  # File I/O
│   │   ├── StockETFReader.java
│   │   └── BenchmarkReader.java
│   ├── ui/                    # User interface
│   │   └── SimulatorUI.java
│   └── Main.java
├── test/project/              # Unit tests
│   ├── BestWorstPerformersTest.java
│   ├── BreakoutTradingPatternTest.java
│   ├── MomentumTradingPatternTest.java
│   ├── TotalValueOnDateTest.java
│   └── TradingPatternTest.java
├── CSVs/                      # Data directory (not included)
├── README.md
├── SETUP.md
├── LICENSE
└── .gitignore
```

## Getting Started

### Prerequisites

- Java 11+
- CSV files with stock/ETF data in `CSVs/` directory
  - Format: `{TICKER} {Stock|ETF}.csv` (e.g., `AAPL Stock.csv`)
  - Required columns: Date, Open, High, Low, Close, Volume
  - S&P 500 benchmark file: `sap500indexbenchmark.csv`

### Running the Application

1. **Compile** (if not using an IDE):
   ```bash
   javac -d bin src/project/**/*.java
   ```

2. **Run**:
   ```bash
   java -cp bin project.Main
   ```

3. **Use the interactive UI** to:
   - Input initial investment amount
   - Select stocks/ETFs to trade
   - Set allocations, stop-loss, and profit targets
   - Choose a trading strategy
   - View results and performance metrics

### Running Tests

```bash
javac -cp .:junit-4.13.jar test/project/*.java
java -cp .:junit-4.13.jar org.junit.runner.JUnitCore project.YourTestClass
```

## Architecture

### Design Patterns

- **Strategy Pattern**: `TradingStrategy` interface allows swapping trading algorithms
- **Singleton Pattern**: `StockETFReader`, `BenchmarkReader` for shared data access
- **N-Tier Architecture**: Data layer → Domain layer → Business logic → Presentation

### Key Classes

**Portfolio** — Core state container
- Tracks positions, cash, allocations
- Stores buy/sell prices and performance metrics
- Can execute different trading strategies

**StockETF / Investment** — Represents individual securities
- Stores historical prices in a TreeMap (date-sorted)
- Inherits from abstract `Investment` class

**TradingStrategy** — Interface for pluggable strategies
- Implementations: `TradingPattern`, `MomentumTradingPattern`, `BreakoutTradingPattern`
- Each runs a complete backtest on the portfolio

**DateResultMap** — Immutable performance snapshots
- Records daily portfolio state (cash, shares value, return %)
- Enables historical performance tracking

## Usage Example

```java
// Create portfolio with initial investment
Portfolio portfolio = new Portfolio(
    10000.0,           // Initial investment
    allocations,       // Map of ticker -> allocation %
    stopLoss,          // Stop-loss % 
    riskPercent,       // Risk per trade %
    targetPercent,     // Profit target %
    thresholdPrices,   // Buy threshold per stock
    stocks,            // Map of tickers -> StockETF objects
    shares,            // Current positions
    dateResults        // Historical snapshots
);

// Execute momentum strategy
TradingStrategy strategy = new MomentumTradingPattern();
strategy.RunTradingPattern(portfolio);

// Compare with S&P 500
CompareWithSP500 spComparison = new CompareWithSP500();
spComparison.execute(portfolio, startDate, endDate);

// Get results
System.out.println("Total Return: " + portfolio.getTotalReturnPercentage() + "%");
System.out.println("S&P 500 vs Portfolio: " + portfolio.getSPReturnOverUnder() + "%");
```

## Known Limitations

- **CSV-dependent**: Requires properly formatted CSV files in exact directory structure
- **Date validation**: Only supports YYYY-MM-DD format between 2020-2025
- **Single-threaded**: No parallel processing for multiple portfolios
- **No persistence**: Results not saved to database; only printed to console
- **Limited UI**: Text-based interface only; no visualization

## Future Enhancements

- [ ] Database persistence (PostgreSQL, MongoDB)
- [ ] REST API for programmatic access
- [ ] Web dashboard with charting (React/D3.js)
- [ ] Machine learning-based strategy optimization
- [ ] Real-time data integration (Alpha Vantage, Yahoo Finance API)
- [ ] Multi-threaded backtesting
- [ ] Support for more date ranges and historical data

## Contributing

Pull requests welcome. For major changes, please open an issue first to discuss proposed changes.

## Testing

Unit tests included in `test/` directory using JUnit 4. Tests demonstrate approaches for null input validation, portfolio state verification, and trading pattern execution.

## Notes on Code Quality

This project was developed as an **Object-Oriented Design coursework** demonstrating:
- Multi-layer architecture
- Design patterns (Strategy, Singleton)
- Proper encapsulation and abstraction
- Exception handling
- Data structure selection (TreeMap for time-series data)

## License

MIT License — see LICENSE file

## Contact

Jun — [GitHub](https://github.com/yourusername)

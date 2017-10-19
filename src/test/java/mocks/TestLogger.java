package mocks;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.Marker;

public class TestLogger implements Logger {

	private List<String> warningList;

	public TestLogger() {
		warningList = new ArrayList<String>();
	}

	public List<String> getWarningList() {
		return warningList;
	}

	@Override
	public String getName() {
		throw new IllegalAccessError("Method not implemented");
	}

	@Override
	public boolean isTraceEnabled() {
		throw new IllegalAccessError("Method not implemented");
	}

	@Override
	public void trace(String msg) {
		throw new IllegalAccessError("Method not implemented");
	}

	@Override
	public void trace(String format, Object arg) {
		throw new IllegalAccessError("Method not implemented");
	}

	@Override
	public void trace(String format, Object arg1, Object arg2) {
		throw new IllegalAccessError("Method not implemented");
	}

	@Override
	public void trace(String format, Object... arguments) {
		throw new IllegalAccessError("Method not implemented");
	}

	@Override
	public void trace(String msg, Throwable t) {
		throw new IllegalAccessError("Method not implemented");
	}

	@Override
	public boolean isTraceEnabled(Marker marker) {
		throw new IllegalAccessError("Method not implemented");
	}

	@Override
	public void trace(Marker marker, String msg) {
		throw new IllegalAccessError("Method not implemented");
	}

	@Override
	public void trace(Marker marker, String format, Object arg) {
		throw new IllegalAccessError("Method not implemented");
	}

	@Override
	public void trace(Marker marker, String format, Object arg1, Object arg2) {
		throw new IllegalAccessError("Method not implemented");
	}

	@Override
	public void trace(Marker marker, String format, Object... argArray) {
		throw new IllegalAccessError("Method not implemented");
	}

	@Override
	public void trace(Marker marker, String msg, Throwable t) {
		throw new IllegalAccessError("Method not implemented");
	}

	@Override
	public boolean isDebugEnabled() {
		throw new IllegalAccessError("Method not implemented");
	}

	@Override
	public void debug(String msg) {
		throw new IllegalAccessError("Method not implemented");
	}

	@Override
	public void debug(String format, Object arg) {
		throw new IllegalAccessError("Method not implemented");
	}

	@Override
	public void debug(String format, Object arg1, Object arg2) {
		throw new IllegalAccessError("Method not implemented");
	}

	@Override
	public void debug(String format, Object... arguments) {
		throw new IllegalAccessError("Method not implemented");
	}

	@Override
	public void debug(String msg, Throwable t) {
		throw new IllegalAccessError("Method not implemented");
	}

	@Override
	public boolean isDebugEnabled(Marker marker) {
		throw new IllegalAccessError("Method not implemented");
	}

	@Override
	public void debug(Marker marker, String msg) {
		throw new IllegalAccessError("Method not implemented");
	}

	@Override
	public void debug(Marker marker, String format, Object arg) {
		throw new IllegalAccessError("Method not implemented");
	}

	@Override
	public void debug(Marker marker, String format, Object arg1, Object arg2) {
		throw new IllegalAccessError("Method not implemented");
	}

	@Override
	public void debug(Marker marker, String format, Object... arguments) {
		throw new IllegalAccessError("Method not implemented");
	}

	@Override
	public void debug(Marker marker, String msg, Throwable t) {
		throw new IllegalAccessError("Method not implemented");
	}

	@Override
	public boolean isInfoEnabled() {
		throw new IllegalAccessError("Method not implemented");
	}

	@Override
	public void info(String msg) {
		//ok to use
		System.out.println(msg);
	}

	@Override
	public void info(String format, Object arg) {
		throw new IllegalAccessError("Method not implemented");
	}

	@Override
	public void info(String format, Object arg1, Object arg2) {
		throw new IllegalAccessError("Method not implemented");
	}

	@Override
	public void info(String format, Object... arguments) {
		throw new IllegalAccessError("Method not implemented");
	}

	@Override
	public void info(String msg, Throwable t) {
		throw new IllegalAccessError("Method not implemented");
	}

	@Override
	public boolean isInfoEnabled(Marker marker) {
		throw new IllegalAccessError("Method not implemented");
	}

	@Override
	public void info(Marker marker, String msg) {
		throw new IllegalAccessError("Method not implemented");
	}

	@Override
	public void info(Marker marker, String format, Object arg) {
		throw new IllegalAccessError("Method not implemented");
	}

	@Override
	public void info(Marker marker, String format, Object arg1, Object arg2) {
		throw new IllegalAccessError("Method not implemented");
	}

	@Override
	public void info(Marker marker, String format, Object... arguments) {
		throw new IllegalAccessError("Method not implemented");
	}

	@Override
	public void info(Marker marker, String msg, Throwable t) {
		throw new IllegalAccessError("Method not implemented");
	}

	@Override
	public boolean isWarnEnabled() {
		throw new IllegalAccessError("Method not implemented");
	}

	@Override
	public void warn(String msg) {
		warningList.add(msg);
	}

	@Override
	public void warn(String format, Object arg) {
		warningList.add(format.replaceFirst("\\{\\}", arg.toString()));
	}

	@Override
	public void warn(String format, Object... arguments) {
		throw new IllegalAccessError("Method not implemented");
	}

	@Override
	public void warn(String format, Object arg1, Object arg2) {
		throw new IllegalAccessError("Method not implemented");
	}

	@Override
	public void warn(String msg, Throwable t) {
		throw new IllegalAccessError("Method not implemented");
	}

	@Override
	public boolean isWarnEnabled(Marker marker) {
		throw new IllegalAccessError("Method not implemented");
	}

	@Override
	public void warn(Marker marker, String msg) {
		throw new IllegalAccessError("Method not implemented");
	}

	@Override
	public void warn(Marker marker, String format, Object arg) {
		throw new IllegalAccessError("Method not implemented");
	}

	@Override
	public void warn(Marker marker, String format, Object arg1, Object arg2) {
		throw new IllegalAccessError("Method not implemented");
	}

	@Override
	public void warn(Marker marker, String format, Object... arguments) {
		throw new IllegalAccessError("Method not implemented");
	}

	@Override
	public void warn(Marker marker, String msg, Throwable t) {
		throw new IllegalAccessError("Method not implemented");
	}

	@Override
	public boolean isErrorEnabled() {
		throw new IllegalAccessError("Method not implemented");
	}

	@Override
	public void error(String msg) {
		throw new IllegalAccessError("Method not implemented");
	}

	@Override
	public void error(String format, Object arg) {
		throw new IllegalAccessError("Method not implemented");
	}

	@Override
	public void error(String format, Object arg1, Object arg2) {
		throw new IllegalAccessError("Method not implemented");
	}

	@Override
	public void error(String format, Object... arguments) {
		throw new IllegalAccessError("Method not implemented");
	}

	@Override
	public void error(String msg, Throwable t) {
		throw new IllegalAccessError("Method not implemented");
	}

	@Override
	public boolean isErrorEnabled(Marker marker) {
		throw new IllegalAccessError("Method not implemented");
	}

	@Override
	public void error(Marker marker, String msg) {
		throw new IllegalAccessError("Method not implemented");
	}

	@Override
	public void error(Marker marker, String format, Object arg) {
		throw new IllegalAccessError("Method not implemented");
	}

	@Override
	public void error(Marker marker, String format, Object arg1, Object arg2) {
		throw new IllegalAccessError("Method not implemented");
	}

	@Override
	public void error(Marker marker, String format, Object... arguments) {
		throw new IllegalAccessError("Method not implemented");
	}

	@Override
	public void error(Marker marker, String msg, Throwable t) {
		throw new IllegalAccessError("Method not implemented");
	}

}

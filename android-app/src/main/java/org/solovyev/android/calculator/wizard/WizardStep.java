package org.solovyev.android.calculator.wizard;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;

import org.solovyev.android.App;
import org.solovyev.android.Views;
import org.solovyev.android.calculator.CalculatorApplication;
import org.solovyev.android.calculator.CalculatorLocator;
import org.solovyev.android.calculator.CalculatorPreferences;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static org.solovyev.android.calculator.wizard.ChooseModeWizardStep.MODE;
import static org.solovyev.android.calculator.wizard.ChooseLayoutWizardStep.LAYOUT;
import static org.solovyev.android.calculator.wizard.OnScreenCalculatorWizardStep.ONSCREEN_CALCULATOR_ENABLED;

/**
 * User: serso
 * Date: 6/16/13
 * Time: 9:17 PM
 */
enum WizardStep {

	welcome(WelcomeWizardStep.class) {
		@Override
		boolean onNext(@Nonnull Fragment fragment) {
			return true;
		}

		@Override
		boolean onPrev(@Nonnull Fragment fragment) {
			return true;
		}

		@Nullable
		@Override
		Bundle getFragmentArgs() {
			return null;
		}
	},

	choose_layout(ChooseLayoutWizardStep.class) {
		@Override
		boolean onNext(@Nonnull Fragment f) {
			final ChooseLayoutWizardStep fragment = (ChooseLayoutWizardStep) f;

			final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(f.getActivity());

			final CalculatorLayout layout = fragment.getSelectedLayout();
			layout.apply(preferences);

			return true;
		}

		@Override
		boolean onPrev(@Nonnull Fragment fragment) {
			return true;
		}

		@Nullable
		@Override
		Bundle getFragmentArgs() {
			final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(CalculatorApplication.getInstance());

			final Bundle bundle = new Bundle();
			bundle.putSerializable(LAYOUT, CalculatorLayout.fromGuiLayout(CalculatorPreferences.Gui.layout.getPreference(preferences)));
			return bundle;
		}

		@Override
		public boolean isVisible() {
			return Views.isLayoutSizeAtLeast(Configuration.SCREENLAYOUT_SIZE_LARGE, App.getApplication().getResources().getConfiguration());
		}
	},

	choose_mode(ChooseModeWizardStep.class) {
		@Override
		boolean onNext(@Nonnull Fragment f) {
			final ChooseModeWizardStep fragment = (ChooseModeWizardStep) f;

			final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(f.getActivity());

			final CalculatorMode mode = fragment.getSelectedMode();
			mode.apply(preferences);

			return true;
		}

		@Override
		boolean onPrev(@Nonnull Fragment fragment) {
			return true;
		}

		@Nullable
		@Override
		Bundle getFragmentArgs() {
			final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(CalculatorApplication.getInstance());

			final Bundle bundle = new Bundle();
			bundle.putSerializable(MODE, CalculatorMode.fromGuiLayout(CalculatorPreferences.Gui.layout.getPreference(preferences)));
			return bundle;
		}
	},

	on_screen_calculator(OnScreenCalculatorWizardStep.class) {
		@Override
		boolean onNext(@Nonnull Fragment f) {
			final OnScreenCalculatorWizardStep fragment = (OnScreenCalculatorWizardStep) f;

			final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(f.getActivity());

			CalculatorPreferences.OnscreenCalculator.showAppIcon.putPreference(preferences, fragment.isOnscreenCalculatorEnabled());

			return true;
		}

		@Override
		boolean onPrev(@Nonnull Fragment fragment) {
			return true;
		}

		@Nullable
		@Override
		Bundle getFragmentArgs() {
			final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(CalculatorApplication.getInstance());

			final Bundle bundle = new Bundle();
			bundle.putSerializable(ONSCREEN_CALCULATOR_ENABLED, CalculatorPreferences.OnscreenCalculator.showAppIcon.getPreference(preferences));
			return bundle;
		}
	};

	@Nonnull
	private final Class<? extends Fragment> fragmentClass;

	WizardStep(@Nonnull Class<? extends Fragment> fragmentClass) {
		this.fragmentClass = fragmentClass;
	}

	public String getFragmentTag() {
		return name();
	}

	@Nonnull
	Class<? extends Fragment> getFragmentClass() {
		return fragmentClass;
	}

	abstract boolean onNext(@Nonnull Fragment fragment);

	abstract boolean onPrev(@Nonnull Fragment fragment);

	@Nullable
	abstract Bundle getFragmentArgs();

	public boolean isVisible() {
		return true;
	}
}
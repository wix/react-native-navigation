//
//  DeckSnapshotUpdater.swift
//  DeckTransition
//
//  Created by Harshil Shah on 06/12/17.
//  Copyright © 2017 Harshil Shah. All rights reserved.
//

/// A protocol to communicate to the transition that an update of the snapshot
/// view is required. This is adopted only by the presentation controller of
/// any view controller presented using DeckTransition
public protocol DeckSnapshotUpdater {
    
    /// For various reasons (performance, the way iOS handles safe area,
    /// layout issues, etc.) this transition uses a snapshot view of your
    /// `presentingViewController` and not the live view itself.
    ///
    /// In some cases this snapshot might become outdated before the dismissal,
    /// and for those cases you can request to have the snapshot updated. While
    /// the transition only shows a small portion of the presenting view, in
    /// some cases that might become inconsistent enough to demand an update.
    ///
    /// This is an expensive process and should only be used if necessary, for
    /// example if you are updating your entire app's theme.
    func requestPresentedViewSnapshotUpdate()
}

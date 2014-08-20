//
//  Settings.swift
//  slotmachien
//
//  Created by Feliciaan De Palmenaer on 15/08/14.
//  Copyright (c) 2014 Feliciaan De Palmenaer. All rights reserved.
//

import Foundation

private let _SettingsSharedInstance = Settings()
private let _defaultsUsername = "SMUserName"

class Settings {
    
    class var sharedInstance : Settings {
        return _SettingsSharedInstance
    }
    
    let url: NSURL
    let token: String
    var username: String {
        get {
            let u:String? = defaults.objectForKey(_defaultsUsername) as String?
            if let un = u {
                return un
            }
            return ""
        }
        set(newUsername) {
            defaults.setValue(newUsername, forKey: _defaultsUsername)
            defaults.synchronize()
        }
    }
    
    let defaults = NSUserDefaults(suiteName: "group.be.ugent.zeus")
    
    init() {
        var path = NSBundle.mainBundle().pathForResource("slotmachien", ofType: "plist")
        var dict = NSDictionary(contentsOfFile: path)
        self.url = NSURL(string: dict["url"] as String)
        self.token = dict["token"] as String
    }
    
    private func createRequest(action: String) -> NSURLRequest{
        let options = ["token": token, "user_name": username, "action": action]
        
        let data = NSJSONSerialization.dataWithJSONObject(options, options: nil, error: nil)
        
        var request = NSMutableURLRequest(URL: url)
        request.HTTPMethod = "POST"
        request.HTTPBody = data;
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")
        request.timeoutInterval = 10.0 //10 seconds
        
        return request
    }
    
    func doRequest(action: String, succes: (response: String) -> (), error: (error: String) -> ()) {
        let request = createRequest(action)
        NSURLConnection.sendAsynchronousRequest(request, queue: NSOperationQueue.mainQueue()) {(response, data, requestError) in
            if !requestError {
                let httpResponse = response as NSHTTPURLResponse
                if httpResponse.statusCode == 200 {
                    var jsonError: NSError?
                    let dict: NSDictionary = NSJSONSerialization.JSONObjectWithData(data, options: nil, error: &jsonError) as NSDictionary
                    if let jsonParseError = jsonError {
                        error(error: "Error: json parsing failed")
                    }
                    else if let status:String = dict.objectForKey("status") as? String {
                        succes(response: status)
                    }
                    else {
                        error(error: "Error: json parsing failed")
                    }
                }
                else if httpResponse.statusCode == 401 {
                    error(error: "An authorization error occured")
                }
                else {
                    error(error: "An error occured during the request")
                }
            }
            else {
                error(error: "An error occured during the request")
            }
        }
    }
    
}

enum Status {
    case closed, open, error
    
    // create enum from string
    init(status:String) {
        switch status {
        case "open":
            self = .open
        case "closed", "close":
            self = .closed
        default:
            self = .error
        }
    }
    
    func asAction() -> String {
        switch self {
        case .open:
            return "open"
        case .closed:
            return "close"
        case .error:
            return "status"
        }
    }
    
    mutating func next() {
        switch self {
        case .open:
            self = .closed
        case .closed:
            self = .open
        default:
            self = .error
        }
    }
}